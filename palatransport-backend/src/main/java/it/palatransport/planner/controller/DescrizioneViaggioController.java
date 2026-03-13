package it.palatransport.planner.controller;

import it.palatransport.planner.dto.DescrizioneViaggioRequest;
import it.palatransport.planner.dto.DescrizioneViaggioResponse;
import it.palatransport.planner.service.DescrizioneViaggioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: DescrizioneViaggioController
 *
 * CRUD per le descrizioni dei viaggi (etichette predefinite come "Carico merce").
 * Refactored: ora usa DescrizioneViaggioService e DTO.
 */
@RestController
@RequestMapping("/api/descrizioni-viaggio")
@RequiredArgsConstructor
public class DescrizioneViaggioController {

    private final DescrizioneViaggioService descrizioneViaggioService;

    @GetMapping
    public ResponseEntity<List<DescrizioneViaggioResponse>> getAll() {
        return ResponseEntity.ok(descrizioneViaggioService.getAll());
    }

    /**
     * GET /api/descrizioni-viaggio/attive
     * Restituisce solo le descrizioni attive (per i dropdown del frontend).
     */
    @GetMapping("/attive")
    public ResponseEntity<List<DescrizioneViaggioResponse>> getAllAttive() {
        return ResponseEntity.ok(descrizioneViaggioService.getAllAttive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescrizioneViaggioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(descrizioneViaggioService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DescrizioneViaggioResponse> create(
            @Valid @RequestBody DescrizioneViaggioRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(descrizioneViaggioService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescrizioneViaggioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DescrizioneViaggioRequest request
    ) {
        return ResponseEntity.ok(descrizioneViaggioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        descrizioneViaggioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
