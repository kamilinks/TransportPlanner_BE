package it.palatransport.planner.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponse {
    private Long id;
    private String nome;
    private String ragioneSociale;
    private String partitaIva;
    private String indirizzo;
    private String citta;
    private String cap;
    private String telefono;
    private String email;
    private boolean attivo;
}
