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

// Controller descrizioni viaggi
@RestController
@RequestMapping("/api/descrizioni-viaggio")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DescrizioneViaggioController {

    private final DescrizioneViaggioService descrizioneViaggioService;

    // Elenco descrizioni
    @GetMapping
    public ResponseEntity<List<DescrizioneViaggioResponse>> getAll() {
        return ResponseEntity.ok(descrizioneViaggioService.getAll());
    }

    // Elenco descrizioni attive
    @GetMapping("/attive")
    public ResponseEntity<List<DescrizioneViaggioResponse>> getAllAttive() {
        return ResponseEntity.ok(descrizioneViaggioService.getAllAttive());
    }

    // Dettaglio descrizione per ID
    @GetMapping("/{id}")
    public ResponseEntity<DescrizioneViaggioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(descrizioneViaggioService.getById(id));
    }

    // Creazione descrizione
    @PostMapping
    public ResponseEntity<DescrizioneViaggioResponse> create(
            @Valid @RequestBody DescrizioneViaggioRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(descrizioneViaggioService.create(request));
    }

    // Aggiornamento descrizione
    @PutMapping("/{id}")
    public ResponseEntity<DescrizioneViaggioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DescrizioneViaggioRequest request
    ) {
        return ResponseEntity.ok(descrizioneViaggioService.update(id, request));
    }

    // Eliminazione descrizione
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        descrizioneViaggioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
