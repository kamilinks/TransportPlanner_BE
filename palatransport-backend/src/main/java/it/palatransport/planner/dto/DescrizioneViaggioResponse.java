package it.palatransport.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: DescrizioneViaggioResponse
 *
 * Rappresenta il JSON restituito al client per una descrizione viaggio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescrizioneViaggioResponse {

    private Long id;
    private String codice;
    private String descrizione;
    private boolean attivo;
}
