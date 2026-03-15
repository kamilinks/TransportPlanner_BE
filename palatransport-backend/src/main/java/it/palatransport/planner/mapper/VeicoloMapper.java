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
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface VeicoloMapper {

    @org.mapstruct.Mapping(source = "tipologiaMezzo", target = "tipologiaMezzo")
    VeicoloResponse toResponse(Veicolo veicolo);

    @org.mapstruct.Mapping(source = "tipologiaMezzo", target = "tipologiaMezzo")
    Veicolo fromRequest(VeicoloRequest request);

    @org.mapstruct.Mapping(source = "tipologiaMezzo", target = "tipologiaMezzo")
    void updateFromRequest(VeicoloRequest request, @MappingTarget Veicolo veicolo);
}
