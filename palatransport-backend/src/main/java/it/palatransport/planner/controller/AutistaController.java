package it.palatransport.planner.controller;

import it.palatransport.planner.model.Autista;
import it.palatransport.planner.service.AutistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: AutistaController
 *
 * Espone le API REST CRUD per gli autisti.
 * Questi sono tutti endpoint PROTETTI (richiedono JWT), come definito in SecurityConfig.
 *
 * MAPPA DEGLI ENDPOINT:
 *   GET    /api/autisti        → getAll()    → lista di tutti gli autisti
 *   GET    /api/autisti/{id}   → getById()   → un autista specifico
 *   POST   /api/autisti        → create()    → crea un nuovo autista
 *   PUT    /api/autisti/{id}   → update()    → aggiorna un autista
 *   DELETE /api/autisti/{id}   → delete()    → elimina un autista
 *
 * Questa struttura è la convenzione REST standard:
 *   - Il NOME della risorsa è plurale (/autisti)
 *   - Il METODO HTTP indica l'operazione (GET legge, POST crea, PUT aggiorna, DELETE elimina)
 *   - L'ID va nel PATH per le operazioni su un singolo elemento (/autisti/5)
 */
@RestController
@RequestMapping("/api/autisti")
@RequiredArgsConstructor
public class AutistaController {

    private final AutistaService AutistaService;

    /**
     * GET /api/autisti
     * Risponde con la lista completa degli autisti.
     * HTTP 200 OK + array JSON
     */
    @GetMapping
    public ResponseEntity<List<Autista>> getAll() {
        return ResponseEntity.ok(AutistaService.getAll());
    }

    /**
     * GET /api/autisti/{id}
     *
     * @PathVariable → estrae il valore {id} dall'URL.
     *   Es: GET /api/autisti/5 → id = 5
     *
     * Se l'autista non esiste, il Service lancia RuntimeException,
     * che verrà catturata dal GlobalExceptionHandler e risponderà 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Autista> getById(@PathVariable Long id) {
        return ResponseEntity.ok(AutistaService.getById(id));
    }

    /**
     * POST /api/autisti
     *
     * Crea un nuovo autista.
     * Il client invia: { "nome": "Mario", "cognome": "Rossi", "attivo": true }
     * Il backend assegna automaticamente l'ID.
     *
     * HttpStatus.CREATED (201) è il codice HTTP corretto per una risorsa creata.
     * (Non 200, che è per le letture/aggiornamenti riusciti)
     */
    @PostMapping
    public ResponseEntity<Autista> create(@RequestBody Autista autista) {
        Autista created = AutistaService.create(autista);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/autisti/{id}
     *
     * Aggiorna un autista esistente (sostituzione completa).
     * Il client invia l'oggetto completo aggiornato nel body.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Autista> update(@PathVariable Long id, @RequestBody Autista autista) {
        return ResponseEntity.ok(AutistaService.update(id, autista));
    }

    /**
     * DELETE /api/autisti/{id}
     *
     * Elimina un autista.
     * HTTP 204 No Content: risposta corretta per eliminazioni riuscite (nessun body).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        AutistaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
