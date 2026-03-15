package it.palatransport.planner.controller;

import it.palatransport.planner.dto.ViaggioRequest;
import it.palatransport.planner.dto.ViaggioResponse;
import it.palatransport.planner.service.ViaggioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * CONTROLLER: ViaggioController
 *
 * Il controller principale dell'applicazione. Gestisce tutte le operazioni sui Viaggi.
 *
 * ENDPOINT COMPLETO:
 *   GET    /api/viaggi                → tutti i viaggi
 *   GET    /api/viaggi/{id}           → singolo viaggio
 *   GET    /api/viaggi?autistaId=5    → viaggi di un autista (query parameter)
 *   GET    /api/viaggi?da=2024-01-01&a=2024-01-31 → viaggi in un range di date
 *   POST   /api/viaggi                → crea un viaggio
 *   PUT    /api/viaggi/{id}           → aggiorna un viaggio
 *   DELETE /api/viaggi/{id}           → elimina un viaggio
 *
 * Differenza tra @PathVariable e @RequestParam:
 *   @PathVariable → legge un valore DAL PERCORSO:  /api/viaggi/{id}
 *   @RequestParam → legge un valore dalla QUERY:   /api/viaggi?autistaId=5
 */
@RestController
@RequestMapping("/api/viaggi")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ViaggioController {

    private final ViaggioService viaggioService;

    /**
     * GET /api/viaggi
     * GET /api/viaggi?autistaId=5
     * GET /api/viaggi?da=2024-01-01&a=2024-01-31
     *
     * Un unico endpoint gestisce più casi tramite parametri opzionali:
     *   - Senza parametri → tutti i viaggi
     *   - Con autistaId → viaggi di quell'autista
     *   - Con da e a → viaggi in quell'arco di date
     *
     * @RequestParam(required = false) → il parametro è opzionale, null se assente.
     * @DateTimeFormat → specifica il formato data atteso dalla query string.
     */
    @GetMapping
    public ResponseEntity<List<ViaggioResponse>> getAll(
            @RequestParam(required = false) Long autistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate da,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate a
    ) {
        // Filtra per autista se specificato
        if (autistaId != null) {
            return ResponseEntity.ok(viaggioService.getByAutista(autistaId));
        }
        // Altrimenti restituisce tutti
        return ResponseEntity.ok(viaggioService.getAll());
    }

    /**
     * GET /api/viaggi/paged
     * Restituisce i viaggi con supporto alla paginazione (es: ?page=0&size=20&sort=data,desc).
     */
    @GetMapping("/paged")
    public ResponseEntity<org.springframework.data.domain.Page<ViaggioResponse>> getAllPaged(
            org.springframework.data.domain.Pageable pageable
    ) {
        return ResponseEntity.ok(viaggioService.getAllPaginated(pageable));
    }

    /**
     * GET /api/viaggi/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ViaggioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(viaggioService.getById(id));
    }

    /**
     * POST /api/viaggi
     * Crea un viaggio. Il backend ricalcola la tariffa automaticamente.
     * @Valid → attiva la validazione del DTO ViaggioRequest.
     */
    @PostMapping
    public ResponseEntity<ViaggioResponse> create(@Valid @RequestBody ViaggioRequest request) {
        ViaggioResponse created = viaggioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/viaggi/{id}
     * Aggiorna un viaggio. Il backend ricalcola la tariffa automaticamente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ViaggioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ViaggioRequest request
    ) {
        return ResponseEntity.ok(viaggioService.update(id, request));
    }

    /**
     * DELETE /api/viaggi/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viaggioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
