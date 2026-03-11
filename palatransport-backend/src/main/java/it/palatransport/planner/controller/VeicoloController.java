package it.palatransport.planner.controller;

import it.palatransport.planner.model.Veicolo;
import it.palatransport.planner.repository.VeicoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: VeicoloController
 *
 * CRUD per i veicoli (camion/mezzi dell'azienda).
 * Pattern identico a AutistaController: Get All, Get By Id, Create, Update, Delete.
 *
 * Per semplicità, usiamo direttamente il Repository senza passare per un Service dedicato.
 * In un progetto più grande avremmo un VeicoloService, ma per entità semplici
 * come questa è accettabile mettere la logica minimale nel Controller.
 * (In realtà andrebbero tutti nel Service, ma voglio mostrarti entrambi i pattern)
 */
@RestController
@RequestMapping("/api/veicoli")
@RequiredArgsConstructor
public class VeicoloController {

    // In questo Controller iniettiamo direttamente il Repository (senza Service layer).
    // Questo è il "thin controller" pattern, accettabile quando non c'è business logic.
    private final VeicoloRepository veicoloRepository;

    @GetMapping
    public ResponseEntity<List<Veicolo>> getAll() {
        return ResponseEntity.ok(veicoloRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veicolo> getById(@PathVariable Long id) {
        return veicoloRepository.findById(id)
                .map(ResponseEntity::ok)
                // .map() sul Optional: se presente restituisce 200 OK con il veicolo
                // .orElse() se non presente restituisce 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Veicolo> create(@RequestBody Veicolo veicolo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veicoloRepository.save(veicolo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veicolo> update(@PathVariable Long id, @RequestBody Veicolo aggiornamento) {
        return veicoloRepository.findById(id)
                .map(esistente -> {
                    // Aggiorna i campi dell'entity esistente
                    esistente.setTarga(aggiornamento.getTarga());
                    esistente.setTipo(aggiornamento.getTipo());
                    esistente.setCategoria(aggiornamento.getCategoria());
                    return ResponseEntity.ok(veicoloRepository.save(esistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!veicoloRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        veicoloRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
