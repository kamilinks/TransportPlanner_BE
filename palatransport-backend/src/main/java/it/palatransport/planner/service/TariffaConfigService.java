package it.palatransport.planner.service;

import it.palatransport.planner.dto.TariffaConfigRequest;
import it.palatransport.planner.dto.TariffaConfigResponse;
import it.palatransport.planner.mapper.TariffaConfigMapper;
import it.palatransport.planner.model.TariffaConfig;
import it.palatransport.planner.repository.TariffaConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TariffaConfigService {

    private final TariffaConfigRepository tariffaConfigRepository;
    private final TariffaConfigMapper tariffaConfigMapper;
    
    // Lettura configurazione
    public TariffaConfigResponse get() {
        TariffaConfig config = tariffaConfigRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configurazione tariffe non ancora inizializzata"));
        return tariffaConfigMapper.toResponse(config);
    }

    // Creazione configurazione
    public TariffaConfigResponse create(TariffaConfigRequest request) {
        TariffaConfig config = tariffaConfigMapper.fromRequest(request);
        return tariffaConfigMapper.toResponse(tariffaConfigRepository.save(config));
    }

    // Aggiornamento configurazione
    public TariffaConfigResponse update(TariffaConfigRequest request) {
        TariffaConfig esistente = tariffaConfigRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("Configurazione tariffe non trovata"));
        tariffaConfigMapper.updateFromRequest(request, esistente);
        return tariffaConfigMapper.toResponse(tariffaConfigRepository.save(esistente));
    }
}
