package it.palatransport.planner.service;

import it.palatransport.planner.dto.ViaggioRequest;
import it.palatransport.planner.dto.ViaggioResponse;
import it.palatransport.planner.mapper.ViaggioMapper;
import it.palatransport.planner.model.*;
import it.palatransport.planner.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE: ViaggioService
 *
 * Refactored con MapStruct per il toResponse():
 *   PRIMA: private ViaggioResponse toResponse(Viaggio v) { return ViaggioResponse.builder()... }  (30 righe!)
 *   DOPO:  viaggioMapper.toResponse(v)  ← MapStruct lo genera con i @Mapping nel ViaggioMapper
 *
 * Il fromRequest() rimane manuale perché deve interrogare i Repository per
 * risolvere gli ID (autistaId → Autista entity, veicoloId → Veicolo entity, ecc.).
 * MapStruct non può fare query al database: questo è un limite intenzionale.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ViaggioService {

    private final ViaggioRepository viaggioRepository;
    private final AutistaRepository autistaRepository;
    private final VeicoloRepository veicoloRepository;
    private final DescrizioneViaggioRepository descrizioneViaggioRepository;
    private final ClienteRepository clienteRepository;
    private final TariffaConfigRepository tariffaConfigRepository;
    private final ViaggioMapper viaggioMapper; // ← nuovo: mapper per toResponse

    @Transactional(readOnly = true)
    public List<ViaggioResponse> getAll() {
        return viaggioRepository.findAll()
                .stream()
                .map(viaggioMapper::toResponse) // ← prima era: map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ViaggioResponse getById(Long id) {
        Viaggio viaggio = viaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con id: " + id));
        return viaggioMapper.toResponse(viaggio);
    }

    @Transactional(readOnly = true)
    public List<ViaggioResponse> getByAutista(Long autistaId) {
        return viaggioRepository.findByAutistaId(autistaId)
                .stream()
                .map(viaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ViaggioResponse create(ViaggioRequest request) {
        Viaggio viaggio = fromRequest(request, new Viaggio());
        calcolaTariffa(viaggio, request);
        return viaggioMapper.toResponse(viaggioRepository.save(viaggio));
    }

    public ViaggioResponse update(Long id, ViaggioRequest request) {
        Viaggio esistente = viaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con id: " + id));
        fromRequest(request, esistente);
        calcolaTariffa(esistente, request);
        return viaggioMapper.toResponse(viaggioRepository.save(esistente));
    }

    public void delete(Long id) {
        if (!viaggioRepository.existsById(id)) {
            throw new RuntimeException("Viaggio non trovato con id: " + id);
        }
        viaggioRepository.deleteById(id);
    }

    // =========================================================================
    // METODI PRIVATI — rimangono manuali perché accedono ai Repository
    // =========================================================================

    /**
     * Calcolo tariffe: logica di business che non può essere delegata a MapStruct.
     * Legge la configurazione dal DB e applica le maggiorazioni.
     */
    private void calcolaTariffa(Viaggio viaggio, ViaggioRequest request) {
        TariffaConfig config = tariffaConfigRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    TariffaConfig defaults = new TariffaConfig();
                    defaults.setTariffaBaseKm(1.15);
                    defaults.setMaggiorazioneSabato(50.0);
                    defaults.setMaggiorazioneDomenica(80.0);
                    defaults.setMaggiorazioneBlue(30.0);
                    defaults.setMaggiorazioneSosta(40.0);
                    defaults.setMaggiorazioneFacchinaggio(25.0);
                    defaults.setMaggiorazioneLavAgg(35.0);
                    return defaults;
                });

        double tariffaBase = request.getKm() * config.getTariffaBaseKm();
        viaggio.setTariffaBase(tariffaBase);

        double tariffaTotale = tariffaBase;
        if (request.isSabato())          tariffaTotale += config.getMaggiorazioneSabato();
        if (request.isDomenica())         tariffaTotale += config.getMaggiorazioneDomenica();
        if ("BLUE".equals(request.getTrazione())) tariffaTotale += config.getMaggiorazioneBlue();
        if (request.isSostaNotturna())    tariffaTotale += config.getMaggiorazioneSosta();
        if (request.isFacchinaggio())     tariffaTotale += config.getMaggiorazioneFacchinaggio();
        if (request.isLavoroAggiuntivo()) tariffaTotale += config.getMaggiorazioneLavAgg();

        viaggio.setTariffaTotale(tariffaTotale);
    }

    /**
     * Mapping DTO → Entity per Viaggio.
     * Rimane manuale perché risolve gli ID in Entity tramite query al database.
     * MapStruct non può fare questo.
     */
    private Viaggio fromRequest(ViaggioRequest request, Viaggio viaggio) {
        viaggio.setData(request.getData());
        viaggio.setLuogoPartenza(request.getLuogoPartenza());
        viaggio.setLuogoDestinazione(request.getLuogoDestinazione());
        viaggio.setKm(request.getKm());
        viaggio.setTrazione(request.getTrazione());
        viaggio.setOrario(request.getOrario());
        viaggio.setSabato(request.isSabato());
        viaggio.setDomenica(request.isDomenica());
        viaggio.setSostaNotturna(request.isSostaNotturna());
        viaggio.setFacchinaggio(request.isFacchinaggio());
        viaggio.setLavoroAggiuntivo(request.isLavoroAggiuntivo());
        viaggio.setNote(request.getNote());

        // Risolve gli ID → Entity (richiede query DB: non delegabile a MapStruct)
        Autista autista = autistaRepository.findById(request.getAutistaId())
                .orElseThrow(() -> new RuntimeException("Autista non trovato: " + request.getAutistaId()));
        viaggio.setAutista(autista);

        if (request.getVeicoloSalitaId() != null) {
            viaggio.setVeicoloSalita(veicoloRepository.findById(request.getVeicoloSalitaId()).orElse(null));
        }
        if (request.getVeicoloDiscesaId() != null) {
            viaggio.setVeicoloDiscesa(veicoloRepository.findById(request.getVeicoloDiscesaId()).orElse(null));
        }
        if (request.getDescrizioneSalitaId() != null) {
            viaggio.setDescrizioneSalita(descrizioneViaggioRepository.findById(request.getDescrizioneSalitaId()).orElse(null));
        }
        if (request.getDescrizioneDiscesaId() != null) {
            viaggio.setDescrizioneDiscesa(descrizioneViaggioRepository.findById(request.getDescrizioneDiscesaId()).orElse(null));
        }

        // Risoluzione Clienti
        if (request.getClienteSalitaId() != null) {
            viaggio.setClienteSalita(clienteRepository.findById(request.getClienteSalitaId()).orElse(null));
        }
        if (request.getClienteDiscesaId() != null) {
            viaggio.setClienteDiscesa(clienteRepository.findById(request.getClienteDiscesaId()).orElse(null));
        }

        return viaggio;
    }
}
