package it.palatransport.planner.service;

import it.palatransport.planner.dto.ViaggioRequest;
import it.palatransport.planner.dto.ViaggioResponse;
import it.palatransport.planner.model.*;
import it.palatransport.planner.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE: ViaggioService — Il Servizio Più Complesso
 *
 * Gestisce tutta la logica legata ai Viaggi, incluso:
 *   1. CRUD classico (create, read, update, delete)
 *   2. CALCOLO TARIFFE: ricalcola tariffaBase e tariffaTotale
 *      basandosi sulla TariffaConfig nel database
 *   3. MAPPING Entity → DTO (toResponse) e DTO → Entity (fromRequest)
 *
 * @Transactional: questa annotazione a livello di classe significa che
 *   ogni metodo pubblico viene eseguito in una TRANSAZIONE del database.
 *   Se qualcosa va storto a metà (es. eccezione), la transazione viene
 *   annullata (ROLLBACK) e il database rimane in uno stato coerente.
 *   È fondamentale per operazioni che modificano dati!
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ViaggioService {

    private final ViaggioRepository viaggioRepository;
    private final AutistaRepository AutistaRepository;
    private final VeicoloRepository veicoloRepository;
    private final DescrizioneViaggioRepository descrizioneViaggioRepository;
    private final TariffaConfigRepository tariffaConfigRepository;

    /** Recupera tutti i viaggi convertiti in DTO di risposta */
    @Transactional(readOnly = true) // readOnly = true: ottimizzazione per sola lettura
    public List<ViaggioResponse> getAll() {
        return viaggioRepository.findAll()
                .stream()
                .map(this::toResponse) // converti ogni Viaggio in ViaggioResponse
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ViaggioResponse getById(Long id) {
        Viaggio viaggio = viaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con id: " + id));
        return toResponse(viaggio);
    }

    @Transactional(readOnly = true)
    public List<ViaggioResponse> getByAutista(Long autistaId) {
        return viaggioRepository.findByAutistaId(autistaId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * CREA un nuovo viaggio con calcolo automatico delle tariffe.
     *
     * Flusso:
     *   1. Converti il DTO in Entity (fromRequest) → risolve gli ID in oggetti
     *   2. Calcola la tariffa lato backend (non ci fidiamo del client!)
     *   3. Salva nel database
     *   4. Converti l'Entity salvata in DTO di risposta
     */
    public ViaggioResponse create(ViaggioRequest request) {
        Viaggio viaggio = fromRequest(request, new Viaggio());
        calcolaTariffa(viaggio, request);
        Viaggio saved = viaggioRepository.save(viaggio);
        return toResponse(saved);
    }

    /**
     * AGGIORNA un viaggio esistente.
     */
    public ViaggioResponse update(Long id, ViaggioRequest request) {
        Viaggio esistente = viaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con id: " + id));
        fromRequest(request, esistente); // aggiorna i campi dell'entity esistente
        calcolaTariffa(esistente, request);
        Viaggio updated = viaggioRepository.save(esistente);
        return toResponse(updated);
    }

    /** Elimina un viaggio */
    public void delete(Long id) {
        if (!viaggioRepository.existsById(id)) {
            throw new RuntimeException("Viaggio non trovato con id: " + id);
        }
        viaggioRepository.deleteById(id);
    }

    // =========================================================================
    // METODI PRIVATI DI SUPPORTO
    // =========================================================================

    /**
     * CALCOLO TARIFFE: logica di business.
     *
     * Recupera la configurazione tariffe dal database e calcola:
     *   - tariffaBase = km * tariffaBaseKm
     *   - tariffaTotale = tariffaBase + maggiorazioni applicabili
     *
     * Questo è il metodo che "replica" e sostituisce il km-calculator.service.ts
     * di Angular, ma in modo più sicuro: il calcolo avviene lato server dove
     * il client non può manipolarlo.
     */
    private void calcolaTariffa(Viaggio viaggio, ViaggioRequest request) {
        TariffaConfig config = tariffaConfigRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    // Se non c'è configurazione nel DB, usa valori di default
                    // In un sistema reale questo dovrebbe lanciare un'eccezione
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

        // Calcolo tariffa base dai km
        double tariffaBase = request.getKm() * config.getTariffaBaseKm();
        viaggio.setTariffaBase(tariffaBase);

        // Accumula le maggiorazioni
        double tariffaTotale = tariffaBase;
        if (request.isSabato())         tariffaTotale += config.getMaggiorazioneSabato();
        if (request.isDomenica())        tariffaTotale += config.getMaggiorazioneDomenica();
        if ("BLUE".equals(request.getTrazione())) tariffaTotale += config.getMaggiorazioneBlue();
        if (request.isSostaNotturna())   tariffaTotale += config.getMaggiorazioneSosta();
        if (request.isFacchinaggio())    tariffaTotale += config.getMaggiorazioneFacchinaggio();
        if (request.isLavoroAggiuntivo()) tariffaTotale += config.getMaggiorazioneLavAgg();

        viaggio.setTariffaTotale(tariffaTotale);
    }

    /**
     * MAPPING: ViaggioRequest (DTO) → Viaggio (Entity)
     *
     * Converte il DTO ricevuto dall'API in un'Entity JPA da salvare nel DB.
     * Risolve gli ID degli oggetti correlati (autistaId → Autista entity).
     *
     * @param request il DTO dalla richiesta HTTP
     * @param viaggio l'entity da popolare (nuova per create, esistente per update)
     */
    private Viaggio fromRequest(ViaggioRequest request, Viaggio viaggio) {
        // Imposta i campi semplici
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

        // Risolve gli ID → Oggetti Entity dal database
        Autista autista = AutistaRepository.findById(request.getAutistaId())
                .orElseThrow(() -> new RuntimeException("Autista non trovato: " + request.getAutistaId()));
        viaggio.setAutista(autista);

        // Campi opzionali: controlla se l'ID è valorizzato prima di cercare
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

        return viaggio;
    }

    /**
     * MAPPING: Viaggio (Entity) → ViaggioResponse (DTO)
     *
     * Converte l'Entity del database in un DTO "appiattito" da inviare al client.
     * Naviga le relazioni JPA per estrarre i dati correlati.
     *
     * @param v l'entity Viaggio dal database
     * @return il DTO ViaggioResponse da serializzare come JSON
     */
    private ViaggioResponse toResponse(Viaggio v) {
        return ViaggioResponse.builder()
                .id(v.getId())
                .data(v.getData())
                // Autista: naviga la relazione per estrarre nome e cognome
                .autistaId(v.getAutista() != null ? v.getAutista().getId() : null)
                .autistaNome(v.getAutista() != null ? v.getAutista().getNome() : null)
                .autistaCognome(v.getAutista() != null ? v.getAutista().getCognome() : null)
                // Veicoli: naviga per estrarre la targa
                .veicoloSalitaId(v.getVeicoloSalita() != null ? v.getVeicoloSalita().getId() : null)
                .targaSalita(v.getVeicoloSalita() != null ? v.getVeicoloSalita().getTarga() : null)
                .veicoloDiscesaId(v.getVeicoloDiscesa() != null ? v.getVeicoloDiscesa().getId() : null)
                .targaDiscesa(v.getVeicoloDiscesa() != null ? v.getVeicoloDiscesa().getTarga() : null)
                // Descrizioni: naviga per estrarre il testo
                .descrizioneSalitaId(v.getDescrizioneSalita() != null ? v.getDescrizioneSalita().getId() : null)
                .descrizioneSalita(v.getDescrizioneSalita() != null ? v.getDescrizioneSalita().getDescrizione() : null)
                .descrizioneDiscesaId(v.getDescrizioneDiscesa() != null ? v.getDescrizioneDiscesa().getId() : null)
                .descrizioneDiscesa(v.getDescrizioneDiscesa() != null ? v.getDescrizioneDiscesa().getDescrizione() : null)
                // Campi semplici
                .luogoPartenza(v.getLuogoPartenza())
                .luogoDestinazione(v.getLuogoDestinazione())
                .km(v.getKm())
                .tariffaBase(v.getTariffaBase())
                .tariffaTotale(v.getTariffaTotale())
                .trazione(v.getTrazione())
                .orario(v.getOrario())
                .isSabato(v.isSabato())
                .isDomenica(v.isDomenica())
                .sostaNotturna(v.isSostaNotturna())
                .facchinaggio(v.isFacchinaggio())
                .lavoroAggiuntivo(v.isLavoroAggiuntivo())
                .note(v.getNote())
                .build();
    }
}
