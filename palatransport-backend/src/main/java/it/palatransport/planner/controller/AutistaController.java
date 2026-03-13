package it.palatransport.planner.controller;

import it.palatransport.planner.dto.AutistaRequest;
import it.palatransport.planner.dto.AutistaResponse;
import it.palatransport.planner.service.AutistaService;
import jakarta.validation.Valid;
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
 * Pattern DTO:
 *   - @RequestBody riceve AutistaRequest (dati dal client, senza id)
 *   - ResponseEntity restituisce AutistaResponse (dati per il client, include id)
 */
@RestController
@RequestMapping("/api/autisti")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AutistaController {

    private final AutistaService autistaService;

    /**
     * GET /api/autisti
     * Risponde con la lista completa degli autisti come DTO.
     */
    @GetMapping
    public ResponseEntity<List<AutistaResponse>> getAll() {
        return ResponseEntity.ok(autistaService.getAll());
    }

    /**
     * GET /api/autisti/{id}
     * Recupera un singolo autista per ID.
     * Se non esiste, il Service lancia RuntimeException → GlobalExceptionHandler → 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutistaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(autistaService.getById(id));
    }

    /**
     * POST /api/autisti
     * Crea un nuovo autista.
     * @Valid attiva la validazione delle annotazioni in AutistaRequest (es. @NotBlank).
     * HTTP 201 CREATED è il codice corretto per una risorsa appena creata.
     */
    @PostMapping
    public ResponseEntity<AutistaResponse> create(@Valid @RequestBody AutistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autistaService.create(request));
    }

    /**
     * PUT /api/autisti/{id}
     * Aggiorna un autista esistente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutistaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AutistaRequest request
    ) {
        return ResponseEntity.ok(autistaService.update(id, request));
    }

    /**
     * DELETE /api/autisti/{id}
     * Elimina un autista.
     * HTTP 204 No Content: risposta corretta per eliminazioni riuscite (nessun body).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        autistaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
