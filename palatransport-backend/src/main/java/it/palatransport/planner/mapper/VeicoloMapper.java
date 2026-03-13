package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.VeicoloRequest;
import it.palatransport.planner.dto.VeicoloResponse;
import it.palatransport.planner.model.Veicolo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MAPPER: VeicoloMapper
 *
 * MapStruct gestisce automaticamente la mappatura dell'enum TipoVeicolo
 * (SALITA/DISCESA), perché il tipo è lo stesso sia nella Entity che nel DTO.
 */
@Mapper(componentModel = "spring")
public interface VeicoloMapper {

    VeicoloResponse toResponse(Veicolo veicolo);

    Veicolo fromRequest(VeicoloRequest request);

    void updateFromRequest(VeicoloRequest request, @MappingTarget Veicolo veicolo);
}
