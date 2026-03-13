package it.palatransport.planner.dto;

import it.palatransport.planner.model.Veicolo.TipoVeicolo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO: VeicoloRequest
 *
 * Rappresenta il JSON inviato dal client per creare o modificare un veicolo.
 * Usa direttamente l'enum TipoVeicolo per garantire che il tipo sia valido.
 */
@Data
public class VeicoloRequest {

    @NotBlank(message = "La targa è obbligatoria")
    private String targa;

    @NotNull(message = "Il tipo è obbligatorio (SALITA o DISCESA)")
    private TipoVeicolo tipo;

    private String categoria;
}
