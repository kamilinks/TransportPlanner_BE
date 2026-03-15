package it.palatransport.planner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: DescrizioneViaggioRequest
 *
 * Rappresenta il JSON inviato dal client per creare o modificare una descrizione viaggio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescrizioneViaggioRequest {

    @NotBlank(message = "Il codice è obbligatorio")
    private String codice;

    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    private boolean attivo = true;
}
