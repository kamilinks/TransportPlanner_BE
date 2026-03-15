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

@Service
@Transactional
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class ViaggioService {

    private final ViaggioRepository viaggioRepository;
    private final AutistaRepository autistaRepository;
    private final VeicoloRepository veicoloRepository;
    private final DescrizioneViaggioRepository descrizioneViaggioRepository;
    private final ClienteRepository clienteRepository;
    private final PricingService pricingService;
    private final ViaggioMapper viaggioMapper;

    // Recupera tutti i viaggi
    @Transactional(readOnly = true)
    public List<ViaggioResponse> getAll() {
        log.info("Recupero tutti i viaggi");
        return viaggioRepository.findAll()
                .stream()
                .map(viaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Recupera i viaggi con paginazione
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<ViaggioResponse> getAllPaginated(org.springframework.data.domain.Pageable pageable) {
        log.info("Recupero viaggi paginati: {}", pageable);
        return viaggioRepository.findAll(pageable)
                .map(viaggioMapper::toResponse);
    }

    // Recupera un viaggio per ID
    @Transactional(readOnly = true)
    public ViaggioResponse getById(Long id) {
        log.info("Recupero viaggio con id: {}", id);
        Viaggio viaggio = viaggioRepository.findById(id)
                .orElseThrow(() -> new it.palatransport.planner.exception.ResourceNotFoundException("Viaggio non trovato con id: " + id));
        return viaggioMapper.toResponse(viaggio);
    }

    // Recupera i viaggi di un autista
    @Transactional(readOnly = true)
    public List<ViaggioResponse> getByAutista(Long autistaId) {
        log.info("Recupero viaggi per autista: {}", autistaId);
        return viaggioRepository.findByAutistaId(autistaId)
                .stream()
                .map(viaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Crea un nuovo viaggio
    public ViaggioResponse create(ViaggioRequest request) {
        log.info("Creazione nuovo viaggio per autista id: {}", request.getAutistaId());
        Viaggio viaggio = fromRequest(request, new Viaggio());
        pricingService.calcolaTariffa(viaggio, request);
        return viaggioMapper.toResponse(viaggioRepository.save(viaggio));
    }

    // Aggiorna un viaggio esistente
    public ViaggioResponse update(Long id, ViaggioRequest request) {
        log.info("Aggiornamento viaggio con id: {}", id);
        Viaggio esistente = viaggioRepository.findById(id)
                .orElseThrow(() -> new it.palatransport.planner.exception.ResourceNotFoundException("Viaggio non trovato con id: " + id));
        fromRequest(request, esistente);
        pricingService.calcolaTariffa(esistente, request);
        return viaggioMapper.toResponse(viaggioRepository.save(esistente));
    }

    // Elimina un viaggio
    public void delete(Long id) {
        log.warn("Eliminazione viaggio con id: {}", id);
        if (!viaggioRepository.existsById(id)) {
            throw new it.palatransport.planner.exception.ResourceNotFoundException("Viaggio non trovato con id: " + id);
        }
        viaggioRepository.deleteById(id);
    }

    // Converte la request in entità risolvendo le dipendenze
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

        Autista autista = autistaRepository.findById(request.getAutistaId())
                .orElseThrow(() -> new it.palatransport.planner.exception.ResourceNotFoundException("Autista non trovato: " + request.getAutistaId()));
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

        if (request.getClienteSalitaId() != null) {
            viaggio.setClienteSalita(clienteRepository.findById(request.getClienteSalitaId()).orElse(null));
        }
        if (request.getClienteDiscesaId() != null) {
            viaggio.setClienteDiscesa(clienteRepository.findById(request.getClienteDiscesaId()).orElse(null));
        }

        return viaggio;
    }
}
