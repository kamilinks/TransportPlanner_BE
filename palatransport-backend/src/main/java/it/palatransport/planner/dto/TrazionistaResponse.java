package it.palatransport.planner.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO: TrazionistaResponse
 *
 * Rappresenta il JSON restituito al client per un trazionista.
 */
@Data
@Builder
public class TrazionistaResponse {

    private Long id;
    private String nome;
    private String partitaIva;
    private String indirizzo;
    private String telefono;
    private String email;
}
