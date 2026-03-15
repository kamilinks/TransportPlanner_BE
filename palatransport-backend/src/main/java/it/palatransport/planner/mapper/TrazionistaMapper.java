package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.TrazionistaRequest;
import it.palatransport.planner.dto.TrazionistaResponse;
import it.palatransport.planner.model.Trazionista;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MAPPER: TrazionistaMapper
 *
 * Tutti i campi di Trazionista hanno lo stesso nome nei DTO,
 * quindi MapStruct li mappa automaticamente senza bisogno di @Mapping espliciti.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TrazionistaMapper {

    TrazionistaResponse toResponse(Trazionista trazionista);

    Trazionista fromRequest(TrazionistaRequest request);

    void updateFromRequest(TrazionistaRequest request, @MappingTarget Trazionista trazionista);
}
