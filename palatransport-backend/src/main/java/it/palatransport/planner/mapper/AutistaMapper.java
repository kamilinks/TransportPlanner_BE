package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.AutistaRequest;
import it.palatransport.planner.dto.AutistaResponse;
import it.palatransport.planner.model.Autista;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MAPPER: AutistaMapper
 *
 * MapStruct genera automaticamente a compile-time l'implementazione di questa interfaccia.
 * Il codice generato è equivalente ai metodi toResponse() e fromRequest() che
 * avevamo scritto a mano nel Service.
 *
 * componentModel = "spring" → MapStruct crea un @Component Spring,
 * così possiamo iniettarlo nei Service con @RequiredArgsConstructor.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AutistaMapper {

    /**
     * Converte un'Entity Autista nel DTO di risposta.
     * MapStruct mappa automaticamente i campi con lo stesso nome:
     *   Autista.id       → AutistaResponse.id
     *   Autista.nome     → AutistaResponse.nome
     *   Autista.cognome  → AutistaResponse.cognome
     *   Autista.attivo   → AutistaResponse.attivo
     */
    AutistaResponse toResponse(Autista autista);

    /**
     * Converte il DTO di richiesta in un'Entity Autista.
     * Usato nella creazione: fromRequest(request) → new Autista con i campi popolati.
     */
    Autista fromRequest(AutistaRequest request);

    /**
     * Aggiorna un'Entity esistente con i dati del DTO.
     * @MappingTarget → MapStruct aggiorna l'oggetto esistente invece di crearne uno nuovo.
     * Usato nell'update: non serve creare un nuovo Autista, ma aggiornare quello già persistito.
     */
    void updateFromRequest(AutistaRequest request, @MappingTarget Autista autista);
}
