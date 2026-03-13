package it.palatransport.planner.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO: AutistaResponse
 *
 * Rappresenta il JSON restituito al client dopo un'operazione su un autista.
 * Espone solo i campi che il frontend deve conoscere.
 * L'ID è incluso perché serve al frontend per operazioni successive (update/delete).
 */
@Data
@Builder
public class AutistaResponse {

    private Long id;
    private String nome;
    private String cognome;
    private boolean attivo;
}
