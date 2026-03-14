package it.palatransport.planner.dto;

import it.palatransport.planner.model.Veicolo.TipoVeicolo;
import it.palatransport.planner.model.Veicolo.TipologiaMezzo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: VeicoloResponse
 *
 * Rappresenta il JSON restituito al client per un veicolo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeicoloResponse {

    private Long id;
    private String targa;
    private TipoVeicolo tipo;
    private TipologiaMezzo tipologiaMezzo;
    private String categoria;
}
