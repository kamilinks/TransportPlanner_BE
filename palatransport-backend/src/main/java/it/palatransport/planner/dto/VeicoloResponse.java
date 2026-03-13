package it.palatransport.planner.dto;

import it.palatransport.planner.model.Veicolo.TipoVeicolo;
import lombok.Builder;
import lombok.Data;

/**
 * DTO: VeicoloResponse
 *
 * Rappresenta il JSON restituito al client per un veicolo.
 */
@Data
@Builder
public class VeicoloResponse {

    private Long id;
    private String targa;
    private TipoVeicolo tipo;
    private String categoria;
}
