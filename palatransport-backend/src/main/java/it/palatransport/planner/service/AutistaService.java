package it.palatransport.planner.service;

import it.palatransport.planner.model.Autista;
import it.palatransport.planner.repository.AutistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * SERVICE: AutistaService
 *
 * Contiene la logica CRUD per gli autisti.
 * Questo Service è più semplice di AuthService perché non ha business logic
 * complessa: si limita a delegare le operazioni al Repository.
 *
 * In progetti più grandi qui ci potrebbero essere regole come:
 *   - "Non puoi eliminare un autista se ha viaggi associati"
 *   - "Invia email di notifica quando un autista viene disattivato"
 *   - "Logga ogni modifica per audit"
 *
 * Tutte le operazioni ritornano le Entity direttamente (Autista).
 * Per entità semplici come questa è accettabile. Per entità complesse
 * come Viaggio useremo i DTO di risposta.
 */
@Service
@RequiredArgsConstructor
public class AutistaService {

    private final AutistaRepository AutistaRepository;

    /**
     * Recupera tutti gli autisti dal database.
     * SQL: SELECT * FROM autisti
     */
    public List<Autista> getAll() {
        return AutistaRepository.findAll();
    }

    /**
     * Recupera un autista per ID.
     * Usa orElseThrow: se non esiste, lancia RuntimeException.
     * Il GlobalExceptionHandler (che creeremo) la intercetterà e risponderà 404.
     */
    public Autista getById(Long id) {
        return AutistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
    }

    /**
     * Crea un nuovo autista.
     * L'ID viene assegnato automaticamente da Hibernate/H2 (IDENTITY strategy).
     * @param autista l'oggetto Autista da salvare (senza id)
     * @return l'autista salvato con l'id generato dal DB
     */
    public Autista create(Autista autista) {
        return AutistaRepository.save(autista);
    }

    /**
     * Aggiorna un autista esistente.
     * FLOW:
     *   1. Verifica che l'autista esista (getById lancia eccezione se no)
     *   2. Aggiorna i campi con i nuovi valori
     *   3. Salva → Hibernate fa un UPDATE invece di INSERT perché l'ID è già presente
     */
    public Autista update(Long id, Autista aggiornamento) {
        Autista esistente = getById(id);
        esistente.setNome(aggiornamento.getNome());
        esistente.setCognome(aggiornamento.getCognome());
        esistente.setAttivo(aggiornamento.isAttivo());
        return AutistaRepository.save(esistente);
    }

    /**
     * Elimina un autista per ID.
     * Prima verifica che esista (per dare un errore chiaro), poi lo cancella.
     */
    public void delete(Long id) {
        Autista autista = getById(id); // lancia eccezione se non trovato
        AutistaRepository.delete(autista);
    }
}
