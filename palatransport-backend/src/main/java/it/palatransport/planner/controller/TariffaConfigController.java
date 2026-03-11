package it.palatransport.planner.controller;

import it.palatransport.planner.model.TariffaConfig;
import it.palatransport.planner.repository.TariffaConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLER: TariffaConfigController
 *
 * Gestisce la SINGOLA configurazione tariffe del sistema.
 * Questo controller è diverso dagli altri: non gestisce una lista,
 * ma un UNICO record di configurazione.
 *
 * Endpoint:
 *   GET  /api/tariffa       → legge la configurazione attuale
 *   POST /api/tariffa       → crea la configurazione iniziale (solo la prima volta)
 *   PUT  /api/tariffa       → aggiorna la configurazione (nessun ID nell'URL!)
 */
@RestController
@RequestMapping("/api/tariffa")
@RequiredArgsConstructor
public class TariffaConfigController {

    private final TariffaConfigRepository tariffaConfigRepository;

    /**
     * GET /api/tariffa
     * Restituisce l'unica riga di configurazione tariffe.
     * Se non esiste ancora, risponde 404.
     */
    @GetMapping
    public ResponseEntity<TariffaConfig> get() {
        return tariffaConfigRepository.findFirstByOrderByIdAsc()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/tariffa
     * Crea la configurazione iniziale (da chiamare una sola volta al setup).
     */
    @PostMapping
    public ResponseEntity<TariffaConfig> create(@RequestBody TariffaConfig config) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tariffaConfigRepository.save(config));
    }

    /**
     * PUT /api/tariffa
     * Aggiorna la configurazione tariffe esistente.
     * Non serve l'ID nell'URL perché c'è un solo record.
     */
    @PutMapping
    public ResponseEntity<TariffaConfig> update(@RequestBody TariffaConfig aggiornamento) {
        return tariffaConfigRepository.findFirstByOrderByIdAsc()
                .map(esistente -> {
                    // Aggiorna tutti i campi della configurazione
                    esistente.setTariffaBaseKm(aggiornamento.getTariffaBaseKm());
                    esistente.setMaggiorazioneSabato(aggiornamento.getMaggiorazioneSabato());
                    esistente.setMaggiorazioneDomenica(aggiornamento.getMaggiorazioneDomenica());
                    esistente.setMaggiorazioneBlue(aggiornamento.getMaggiorazioneBlue());
                    esistente.setMaggiorazioneSosta(aggiornamento.getMaggiorazioneSosta());
                    esistente.setMaggiorazioneFacchinaggio(aggiornamento.getMaggiorazioneFacchinaggio());
                    esistente.setMaggiorazioneLavAgg(aggiornamento.getMaggiorazioneLavAgg());
                    return ResponseEntity.ok(tariffaConfigRepository.save(esistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
