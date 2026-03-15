package it.palatransport.planner.service;

import it.palatransport.planner.dto.ViaggioRequest;
import it.palatransport.planner.model.TariffaConfig;
import it.palatransport.planner.model.Viaggio;
import it.palatransport.planner.repository.TariffaConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service dedicato al calcolo delle tariffe.
 * Segue il principio di Single Responsibility (SRP).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PricingService {

    private final TariffaConfigRepository tariffaConfigRepository;

    /**
     * Calcola le tariffe per un viaggio basandosi sulla configurazione nel DB.
     */
    public void calcolaTariffa(Viaggio viaggio, ViaggioRequest request) {
        log.debug("Calcolo tariffa per viaggio del {}", request.getData());
        
        TariffaConfig config = tariffaConfigRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    log.warn("Configurazione tariffe non trovata, uso valori di default.");
                    TariffaConfig defaults = new TariffaConfig();
                    defaults.setTariffaBaseKm(1.15);
                    defaults.setMaggiorazioneSabato(50.0);
                    defaults.setMaggiorazioneDomenica(80.0);
                    defaults.setMaggiorazioneBlue(30.0);
                    defaults.setMaggiorazioneSosta(40.0);
                    defaults.setMaggiorazioneFacchinaggio(25.0);
                    defaults.setMaggiorazioneLavAgg(35.0);
                    return defaults;
                });

        double tariffaBase = request.getKm() * config.getTariffaBaseKm();
        viaggio.setTariffaBase(tariffaBase);

        double tariffaTotale = tariffaBase;
        if (request.isSabato())          tariffaTotale += config.getMaggiorazioneSabato();
        if (request.isDomenica())         tariffaTotale += config.getMaggiorazioneDomenica();
        if ("BLUE".equals(request.getTrazione())) tariffaTotale += config.getMaggiorazioneBlue();
        if (request.isSostaNotturna())    tariffaTotale += config.getMaggiorazioneSosta();
        if (request.isFacchinaggio())     tariffaTotale += config.getMaggiorazioneFacchinaggio();
        if (request.isLavoroAggiuntivo()) tariffaTotale += config.getMaggiorazioneLavAgg();

        viaggio.setTariffaTotale(tariffaTotale);
        log.info("Tariffa calcolata: Base={}, Totale={}", tariffaBase, tariffaTotale);
    }
}
