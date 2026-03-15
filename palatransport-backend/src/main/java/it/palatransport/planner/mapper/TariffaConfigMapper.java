package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.TariffaConfigRequest;
import it.palatransport.planner.dto.TariffaConfigResponse;
import it.palatransport.planner.model.TariffaConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MAPPER: TariffaConfigMapper
 *
 * Tutti i campi tariffari hanno lo stesso nome nell'Entity e nei DTO.
 *
 * Nota: updatedAt non è nel Request (viene gestito da @PrePersist/@PreUpdate
 * nell'Entity, non inviato dal client), quindi MapStruct lo ignora
 * automaticamente nel fromRequest perché non esiste nel DTO sorgente.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TariffaConfigMapper {

    TariffaConfigResponse toResponse(TariffaConfig config);

    TariffaConfig fromRequest(TariffaConfigRequest request);

    void updateFromRequest(TariffaConfigRequest request, @MappingTarget TariffaConfig config);
}
