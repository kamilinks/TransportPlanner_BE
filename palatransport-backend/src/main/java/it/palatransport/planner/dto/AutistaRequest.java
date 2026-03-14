package it.palatransport.planner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO: AutistaRequest
 *
 * Rappresenta il JSON inviato dal client per creare o modificare un autista.
 * NON contiene l'ID (assegnato dal DB) né campi interni JPA.
 *
 * @NotBlank → la stringa non può essere null né vuota ("").
 */
@Data
public class AutistaRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;

    // Il client può specificare se l'autista è attivo (default: true al momento della creazione)
    private boolean attivo = true;

    @JsonProperty("isTrazionista")
    private boolean trazionista;

    private Double tariffaKm;
}
