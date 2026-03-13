package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.DescrizioneViaggioRequest;
import it.palatransport.planner.dto.DescrizioneViaggioResponse;
import it.palatransport.planner.model.DescrizioneViaggio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MAPPER: DescrizioneViaggioMapper
 *
 * Tutti i campi (codice, descrizione, attivo) hanno lo stesso nome
 * nell'Entity e nei DTO: mapping automatico al 100%.
 */
@Mapper(componentModel = "spring")
public interface DescrizioneViaggioMapper {

    DescrizioneViaggioResponse toResponse(DescrizioneViaggio descrizione);

    DescrizioneViaggio fromRequest(DescrizioneViaggioRequest request);

    void updateFromRequest(DescrizioneViaggioRequest request, @MappingTarget DescrizioneViaggio descrizione);
}
