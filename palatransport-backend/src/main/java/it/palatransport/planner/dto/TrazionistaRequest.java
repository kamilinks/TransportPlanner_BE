package it.palatransport.planner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO: TrazionistaRequest
 *
 * Rappresenta il JSON inviato dal client per creare o modificare un trazionista.
 */
@Data
public class TrazionistaRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    private String partitaIva;

    private String indirizzo;

    private String telefono;

    @Email(message = "Formato email non valido")
    private String email;
}
