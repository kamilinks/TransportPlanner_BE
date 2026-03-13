package it.palatransport.planner.controller;

import it.palatransport.planner.dto.TariffaConfigRequest;
import it.palatransport.planner.dto.TariffaConfigResponse;
import it.palatransport.planner.service.TariffaConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLER: TariffaConfigController
 *
 * Gestisce la SINGOLA configurazione tariffe del sistema.
 * Refactored: ora usa TariffaConfigService e DTO invece di accedere
 * direttamente al repository.
 *
 * Endpoint:
 *   GET  /api/tariffa       → legge la configurazione attuale
 *   POST /api/tariffa       → crea la configurazione iniziale (solo la prima volta)
 *   PUT  /api/tariffa       → aggiorna la configurazione (nessun ID nell'URL)
 */
@RestController
@RequestMapping("/api/tariffa")
@RequiredArgsConstructor
public class TariffaConfigController {

    private final TariffaConfigService tariffaConfigService;

    /**
     * GET /api/tariffa
     * Restituisce l'unica riga di configurazione tariffe.
     */
    @GetMapping
    public ResponseEntity<TariffaConfigResponse> get() {
        return ResponseEntity.ok(tariffaConfigService.get());
    }

    /**
     * POST /api/tariffa
     * Crea la configurazione iniziale (da chiamare una sola volta al setup).
     */
    @PostMapping
    public ResponseEntity<TariffaConfigResponse> create(@Valid @RequestBody TariffaConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffaConfigService.create(request));
    }

    /**
     * PUT /api/tariffa
     * Aggiorna la configurazione tariffe esistente.
     * Non serve l'ID nell'URL perché c'è un solo record.
     */
    @PutMapping
    public ResponseEntity<TariffaConfigResponse> update(@Valid @RequestBody TariffaConfigRequest request) {
        return ResponseEntity.ok(tariffaConfigService.update(request));
    }
}
