package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.ClienteRequest;
import it.palatransport.planner.dto.ClienteResponse;
import it.palatransport.planner.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ClienteMapper {
    ClienteResponse toResponse(Cliente cliente);
    Cliente toEntity(ClienteRequest request);
    void updateEntity(ClienteRequest request, @MappingTarget Cliente cliente);
}
