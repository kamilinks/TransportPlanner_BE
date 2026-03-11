package it.palatransport.planner.controller;

import it.palatransport.planner.model.Trazionista;
import it.palatransport.planner.repository.TrazionistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trazionisti")
@RequiredArgsConstructor
public class TrazionistaController {

    private final TrazionistaRepository trazionistaRepository;

    @GetMapping
    public ResponseEntity<List<Trazionista>> getAll() {
        return ResponseEntity.ok(trazionistaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trazionista> getById(@PathVariable Long id) {
        return trazionistaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Trazionista> create(@RequestBody Trazionista trazionista) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trazionistaRepository.save(trazionista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trazionista> update(@PathVariable Long id, @RequestBody Trazionista aggiornamento) {
        return trazionistaRepository.findById(id)
                .map(esistente -> {
                    esistente.setNome(aggiornamento.getNome());
                    esistente.setPartitaIva(aggiornamento.getPartitaIva());
                    esistente.setIndirizzo(aggiornamento.getIndirizzo());
                    esistente.setTelefono(aggiornamento.getTelefono());
                    esistente.setEmail(aggiornamento.getEmail());
                    return ResponseEntity.ok(trazionistaRepository.save(esistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!trazionistaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        trazionistaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
