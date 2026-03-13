package it.palatransport.planner.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO: DescrizioneViaggioResponse
 *
 * Rappresenta il JSON restituito al client per una descrizione viaggio.
 */
@Data
@Builder
public class DescrizioneViaggioResponse {

    private Long id;
    private String codice;
    private String descrizione;
    private boolean attivo;
}
