package it.palatransport.planner.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO: TariffaConfigRequest
 *
 * Rappresenta il JSON inviato dal client per creare o aggiornare la configurazione tariffe.
 * NON contiene id, né updatedAt (gestito automaticamente lato server con @PrePersist/@PreUpdate).
 *
 * @Positive → il valore deve essere > 0 (le tariffe non possono essere negative o zero).
 */
@Data
public class TariffaConfigRequest {

    @NotNull(message = "La tariffa base al km è obbligatoria")
    @Positive(message = "La tariffa base al km deve essere positiva")
    private Double tariffaBaseKm;

    @NotNull
    @Positive
    private Double maggiorazioneSabato;

    @NotNull
    @Positive
    private Double maggiorazioneDomenica;

    @NotNull
    @Positive
    private Double maggiorazioneBlue;

    @NotNull
    @Positive
    private Double maggiorazioneSosta;

    @NotNull
    @Positive
    private Double maggiorazioneFacchinaggio;

    @NotNull
    @Positive
    private Double maggiorazioneLavAgg;
}
