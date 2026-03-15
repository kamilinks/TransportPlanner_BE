package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.ViaggioResponse;
import it.palatransport.planner.model.Viaggio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Mapper per la conversione dell'entità Viaggio nel suo DTO di risposta
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ViaggioMapper {

    @Mapping(source = "autista.id",      target = "autistaId")
    @Mapping(source = "autista.nome",    target = "autistaNome")
    @Mapping(source = "autista.cognome", target = "autistaCognome")

    @Mapping(source = "veicoloSalita.id",    target = "veicoloSalitaId")
    @Mapping(source = "veicoloSalita.targa", target = "targaSalita")

    @Mapping(source = "veicoloDiscesa.id",    target = "veicoloDiscesaId")
    @Mapping(source = "veicoloDiscesa.targa", target = "targaDiscesa")

    @Mapping(source = "descrizioneSalita.id",          target = "descrizioneSalitaId")
    @Mapping(source = "descrizioneSalita.descrizione", target = "descrizioneSalita")

    @Mapping(source = "descrizioneDiscesa.id",          target = "descrizioneDiscesaId")
    @Mapping(source = "descrizioneDiscesa.descrizione", target = "descrizioneDiscesa")

    @Mapping(source = "clienteSalita.id",   target = "clienteSalitaId")
    @Mapping(source = "clienteSalita.nome", target = "clienteSalitaNome")
    @Mapping(source = "clienteDiscesa.id",   target = "clienteDiscesaId")
    @Mapping(source = "clienteDiscesa.nome", target = "clienteDiscesaNome")

    ViaggioResponse toResponse(Viaggio viaggio);
}
