package it.palatransport.planner.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO: TariffaConfigResponse
 *
 * Rappresenta il JSON restituito al client con la configurazione tariffe corrente.
 * Include updatedAt che indica quando la config è stata modificata l'ultima volta.
 */
@Data
@Builder
public class TariffaConfigResponse {

    private Long id;
    private Double tariffaBaseKm;
    private Double maggiorazioneSabato;
    private Double maggiorazioneDomenica;
    private Double maggiorazioneBlue;
    private Double maggiorazioneSosta;
    private Double maggiorazioneFacchinaggio;
    private Double maggiorazioneLavAgg;
    private LocalDateTime updatedAt;
}
