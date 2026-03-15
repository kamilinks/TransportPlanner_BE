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

// Controller REST per la gestione del parco veicolare
@RestController
@RequestMapping("/api/veicoli")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class VeicoloController {

    private final VeicoloService veicoloService;

    // Recupera la lista di tutti i veicoli registrati
    @GetMapping
    public ResponseEntity<List<VeicoloResponse>> getAll() {
        return ResponseEntity.ok(veicoloService.getAll());
    }

    // Recupera i dettagli di un singolo veicolo tramite il suo ID
    @GetMapping("/{id}")
    public ResponseEntity<VeicoloResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(veicoloService.getById(id));
    }

    // Registra un nuovo veicolo nel database
    @PostMapping
    public ResponseEntity<VeicoloResponse> create(@Valid @RequestBody VeicoloRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veicoloService.create(request));
    }

    // Aggiorna le informazioni di un veicolo esistente tramite ID
    @PutMapping("/{id}")
    public ResponseEntity<VeicoloResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VeicoloRequest request
    ) {
        return ResponseEntity.ok(veicoloService.update(id, request));
    }

    // Elimina un veicolo dal sistema tramite ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veicoloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
