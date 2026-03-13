package it.palatransport.planner.mapper;

import it.palatransport.planner.dto.ViaggioResponse;
import it.palatransport.planner.model.Viaggio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MAPPER: ViaggioMapper — il più complesso
 *
 * Il Viaggio ha relazioni @ManyToOne verso altre Entity (Autista, Veicolo,
 * DescrizioneViaggio). MapStruct non sa automaticamente come navigare queste
 * relazioni per costruire il DTO "appiattito".
 *
 * Con @Mapping(source = "...", target = "...") diciamo esplicitamente:
 *   - source: da dove prendere il valore (navigando la relazione JPA)
 *   - target: dove metterlo nel DTO
 *
 * Esempi:
 *   source = "autista.id"      → naviga viaggio.getAutista().getId()
 *   source = "autista.nome"    → naviga viaggio.getAutista().getNome()
 *   source = "veicoloSalita.targa" → naviga viaggio.getVeicoloSalita().getTarga()
 *
 * NOTA: il fromRequest (DTO → Entity) NON è qui perché richiede query al database
 * per risolvere gli ID in oggetti Entity (es. autistaId → Autista).
 * Quella logica rimane nel ViaggioService dove possiamo accedere ai Repository.
 */
@Mapper(componentModel = "spring")
public interface ViaggioMapper {

    // --- Dati Autista (navigazione relazione @ManyToOne) ---
    @Mapping(source = "autista.id",      target = "autistaId")
    @Mapping(source = "autista.nome",    target = "autistaNome")
    @Mapping(source = "autista.cognome", target = "autistaCognome")

    // --- Dati Veicolo Salita ---
    @Mapping(source = "veicoloSalita.id",    target = "veicoloSalitaId")
    @Mapping(source = "veicoloSalita.targa", target = "targaSalita")

    // --- Dati Veicolo Discesa ---
    @Mapping(source = "veicoloDiscesa.id",    target = "veicoloDiscesaId")
    @Mapping(source = "veicoloDiscesa.targa", target = "targaDiscesa")

    // --- Dati Descrizione Salita ---
    @Mapping(source = "descrizioneSalita.id",          target = "descrizioneSalitaId")
    @Mapping(source = "descrizioneSalita.descrizione", target = "descrizioneSalita")

    // --- Dati Descrizione Discesa ---
    @Mapping(source = "descrizioneDiscesa.id",          target = "descrizioneDiscesaId")
    @Mapping(source = "descrizioneDiscesa.descrizione", target = "descrizioneDiscesa")

    ViaggioResponse toResponse(Viaggio viaggio);
}
