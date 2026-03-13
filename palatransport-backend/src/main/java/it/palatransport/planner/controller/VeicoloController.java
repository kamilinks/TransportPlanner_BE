package it.palatransport.planner.controller;

import it.palatransport.planner.dto.VeicoloRequest;
import it.palatransport.planner.dto.VeicoloResponse;
import it.palatransport.planner.service.VeicoloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: VeicoloController
 *
 * CRUD per i veicoli.
 * Refactored: ora usa VeicoloService e DTO invece di accedere
 * direttamente al repository con l'entity grezza.
 */
@RestController
@RequestMapping("/api/veicoli")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class VeicoloController {

    private final VeicoloService veicoloService;

    @GetMapping
    public ResponseEntity<List<VeicoloResponse>> getAll() {
        return ResponseEntity.ok(veicoloService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeicoloResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(veicoloService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VeicoloResponse> create(@Valid @RequestBody VeicoloRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veicoloService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeicoloResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VeicoloRequest request
    ) {
        return ResponseEntity.ok(veicoloService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veicoloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
