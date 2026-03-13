package it.palatransport.planner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequest {
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    private String ragioneSociale;
    private String partitaIva;
    private String indirizzo;
    private String citta;
    private String cap;
    private String telefono;
    private String email;
    private boolean attivo = true;
}
