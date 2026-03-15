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

// Controller autisti
@RestController
@RequestMapping("/api/autisti")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AutistaController {

    private final AutistaService autistaService;

    // Elenco autisti
    @GetMapping
    public ResponseEntity<List<AutistaResponse>> getAll() {
        return ResponseEntity.ok(autistaService.getAll());
    }

    // Dettaglio autista per ID
    @GetMapping("/{id}")
    public ResponseEntity<AutistaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(autistaService.getById(id));
    }

    // Creazione autista
    @PostMapping
    public ResponseEntity<AutistaResponse> create(@Valid @RequestBody AutistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autistaService.create(request));
    }

    // Aggiornamento autista
    @PutMapping("/{id}")
    public ResponseEntity<AutistaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AutistaRequest request
    ) {
        return ResponseEntity.ok(autistaService.update(id, request));
    }

    // Eliminazione autista
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        autistaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
