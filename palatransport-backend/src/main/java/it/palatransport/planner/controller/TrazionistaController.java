package it.palatransport.planner.controller;

import it.palatransport.planner.dto.TrazionistaRequest;
import it.palatransport.planner.dto.TrazionistaResponse;
import it.palatransport.planner.service.TrazionistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller trazionisti
@RestController
@RequestMapping("/api/trazionisti")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class TrazionistaController {

    private final TrazionistaService trazionistaService;

    // Elenco trazionisti
    @GetMapping
    public ResponseEntity<List<TrazionistaResponse>> getAll() {
        return ResponseEntity.ok(trazionistaService.getAll());
    }

    // Dettaglio trazionista per ID
    @GetMapping("/{id}")
    public ResponseEntity<TrazionistaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(trazionistaService.getById(id));
    }

    // Creazione trazionista
    @PostMapping
    public ResponseEntity<TrazionistaResponse> create(@Valid @RequestBody TrazionistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trazionistaService.create(request));
    }

    // Aggiornamento trazionista
    @PutMapping("/{id}")
    public ResponseEntity<TrazionistaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TrazionistaRequest request
    ) {
        return ResponseEntity.ok(trazionistaService.update(id, request));
    }

    // Eliminazione trazionista
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trazionistaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
