package it.palatransport.planner.dto;

import it.palatransport.planner.model.Veicolo.TipoVeicolo;
import it.palatransport.planner.model.Veicolo.TipologiaMezzo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: VeicoloRequest
 *
 * Rappresenta il JSON inviato dal client per creare o modificare un veicolo.
 * Usa direttamente l'enum TipoVeicolo per garantire che il tipo sia valido.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeicoloRequest {

    @NotBlank(message = "La targa è obbligatoria")
    private String targa;

    @NotNull(message = "Il tipo è obbligatorio (SALITA o DISCESA)")
    private TipoVeicolo tipo;

    private TipologiaMezzo tipologiaMezzo;

    private String categoria;
}
