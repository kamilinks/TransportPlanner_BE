package it.palatransport.planner.controller;

import it.palatransport.planner.dto.ClienteRequest;
import it.palatransport.planner.dto.ClienteResponse;
import it.palatransport.planner.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller clienti
@RestController
@RequestMapping("/api/clienti")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // Elenco tutti i clienti
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAll() {
        return ResponseEntity.ok(clienteService.getAll());
    }

    // Elenco clienti attivi
    @GetMapping("/attivi")
    public ResponseEntity<List<ClienteResponse>> getAttivi() {
        return ResponseEntity.ok(clienteService.getAttivi());
    }

    // Dettaglio cliente per ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    // Creazione cliente
    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(request));
    }

    // Aggiornamento cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> update(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.update(id, request));
    }

    // Eliminazione cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
