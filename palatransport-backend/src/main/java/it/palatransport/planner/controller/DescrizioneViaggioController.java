package it.palatransport.planner.controller;

import it.palatransport.planner.model.DescrizioneViaggio;
import it.palatransport.planner.repository.DescrizioneViaggioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: DescrizioneViaggioController
 *
 * CRUD per le descrizioni dei viaggi (etichette predefinite come "Carico merce").
 */
@RestController
@RequestMapping("/api/descrizioni-viaggio")
@RequiredArgsConstructor
public class DescrizioneViaggioController {

    private final DescrizioneViaggioRepository descrizioneViaggioRepository;

    @GetMapping
    public ResponseEntity<List<DescrizioneViaggio>> getAll() {
        return ResponseEntity.ok(descrizioneViaggioRepository.findAll());
    }

    /**
     * GET /api/descrizioni-viaggio/attive
     * Endpoint aggiuntivo utile per il frontend: restituisce solo le descrizioni
     * "attive" da mostrare nei dropdown.
     */
    @GetMapping("/attive")
    public ResponseEntity<List<DescrizioneViaggio>> getAllAttive() {
        return ResponseEntity.ok(descrizioneViaggioRepository.findByAttivo(true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescrizioneViaggio> getById(@PathVariable Long id) {
        return descrizioneViaggioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DescrizioneViaggio> create(@RequestBody DescrizioneViaggio descrizione) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(descrizioneViaggioRepository.save(descrizione));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescrizioneViaggio> update(
            @PathVariable Long id,
            @RequestBody DescrizioneViaggio aggiornamento
    ) {
        return descrizioneViaggioRepository.findById(id)
                .map(esistente -> {
                    esistente.setCodice(aggiornamento.getCodice());
                    esistente.setDescrizione(aggiornamento.getDescrizione());
                    esistente.setAttivo(aggiornamento.isAttivo());
                    return ResponseEntity.ok(descrizioneViaggioRepository.save(esistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!descrizioneViaggioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        descrizioneViaggioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
