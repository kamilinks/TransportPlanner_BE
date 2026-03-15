package it.palatransport.planner.service;

import it.palatransport.planner.dto.ViaggioRequest;
import it.palatransport.planner.model.TariffaConfig;
import it.palatransport.planner.model.Viaggio;
import it.palatransport.planner.repository.TariffaConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    private TariffaConfigRepository tariffaConfigRepository;

    @InjectMocks
    private PricingService pricingService;

    private TariffaConfig config;

    @BeforeEach
    void setUp() {
        config = new TariffaConfig();
        config.setTariffaBaseKm(1.15);
        config.setMaggiorazioneSabato(50.0);
        config.setMaggiorazioneDomenica(80.0);
        config.setMaggiorazioneBlue(30.0);
        config.setMaggiorazioneSosta(10.0);
        config.setMaggiorazioneFacchinaggio(5.0);
        config.setMaggiorazioneLavAgg(2.0);
    }

    @Test
    void testCalcoloTariffaBase() {
        when(tariffaConfigRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(config));

        Viaggio v = new Viaggio();
        ViaggioRequest req = new ViaggioRequest();
        req.setKm(100.0);

        pricingService.calcolaTariffa(v, req);

        assertEquals(115.0, v.getTariffaBase());
        assertEquals(115.0, v.getTariffaTotale());
    }

    @Test
    void testCalcoloTariffaConMaggiorazioni() {
        when(tariffaConfigRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(config));

        Viaggio v = new Viaggio();
        ViaggioRequest req = new ViaggioRequest();
        req.setKm(100.0);
        req.setTrazione("BLUE");
        req.setSabato(true);

        pricingService.calcolaTariffa(v, req);

        // Base: 100 * 1.15 = 115
        // Blue: +30
        // Sabato: +50
        // Totale: 115 + 30 + 50 = 195
        assertEquals(115.0, v.getTariffaBase());
        assertEquals(195.0, v.getTariffaTotale());
    }
}
