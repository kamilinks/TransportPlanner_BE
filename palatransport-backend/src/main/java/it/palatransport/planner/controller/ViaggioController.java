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

// Controller viaggi
@RestController
@RequestMapping("/api/viaggi")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ViaggioController {

    private final ViaggioService viaggioService;

    // Elenco viaggi con filtri autista/date
    @GetMapping
    public ResponseEntity<List<ViaggioResponse>> getAll(
            @RequestParam(required = false) Long autistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate da,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate a
    ) {
        if (autistaId != null) {
            return ResponseEntity.ok(viaggioService.getByAutista(autistaId));
        }
        return ResponseEntity.ok(viaggioService.getAll());
    }

    // Elenco viaggi paginato
    @GetMapping("/paged")
    public ResponseEntity<org.springframework.data.domain.Page<ViaggioResponse>> getAllPaged(
            org.springframework.data.domain.Pageable pageable
    ) {
        return ResponseEntity.ok(viaggioService.getAllPaginated(pageable));
    }

    // Dettaglio viaggio per ID
    @GetMapping("/{id}")
    public ResponseEntity<ViaggioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(viaggioService.getById(id));
    }

    // Creazione viaggio
    @PostMapping
    public ResponseEntity<ViaggioResponse> create(@Valid @RequestBody ViaggioRequest request) {
        ViaggioResponse created = viaggioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Aggiornamento viaggio
    @PutMapping("/{id}")
    public ResponseEntity<ViaggioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ViaggioRequest request
    ) {
        return ResponseEntity.ok(viaggioService.update(id, request));
    }

    // Eliminazione viaggio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viaggioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
