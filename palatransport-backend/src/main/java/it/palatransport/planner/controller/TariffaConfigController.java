package it.palatransport.planner.controller;

import it.palatransport.planner.dto.TariffaConfigRequest;
import it.palatransport.planner.dto.TariffaConfigResponse;
import it.palatransport.planner.service.TariffaConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller configurazione tariffe
@RestController
@RequestMapping("/api/tariffa")
@RequiredArgsConstructor
public class TariffaConfigController {

    private final TariffaConfigService tariffaConfigService;

    // Lettura configurazione
    @GetMapping
    public ResponseEntity<TariffaConfigResponse> get() {
        return ResponseEntity.ok(tariffaConfigService.get());
    }

    // Setup iniziale configurazione
    @PostMapping
    public ResponseEntity<TariffaConfigResponse> create(@Valid @RequestBody TariffaConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffaConfigService.create(request));
    }

    // Aggiornamento configurazione
    @PutMapping
    public ResponseEntity<TariffaConfigResponse> update(@Valid @RequestBody TariffaConfigRequest request) {
        return ResponseEntity.ok(tariffaConfigService.update(request));
    }
}
