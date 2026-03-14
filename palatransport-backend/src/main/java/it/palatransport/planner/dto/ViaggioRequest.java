package it.palatransport.planner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO: ViaggioRequest
 *
 * Rappresenta il corpo JSON per creare o modificare un viaggio.
 * Il client Angular invia questo oggetto nel body delle richieste POST/PUT.
 *
 * NOTA sulle chiavi esterne (FK):
 * Il client invia solo gli ID (es. autistaId: 2), non l'oggetto completo.
 * Il servizio poi usa questi ID per caricare le Entity complete dal database.
 *
 * NOTA sulle validazioni:
 * @NotNull → non può essere null (per tipi non-String come Long, Double)
 * @NotBlank → non può essere null né stringa vuota (per String)
 */
@Data
public class ViaggioRequest {

    @NotNull(message = "La data è obbligatoria")
    private LocalDate data;

    @NotNull(message = "L'autista è obbligatorio")
    private Long autistaId;

    // Veicolo salita/discesa: opzionali
    private Long veicoloSalitaId;
    private Long veicoloDiscesaId;

    // Descrizioni: opzionali
    private Long descrizioneSalitaId;
    private Long descrizioneDiscesaId;

    // Clienti
    private Long clienteSalitaId;
    private Long clienteDiscesaId;

    @NotBlank(message = "Il luogo di partenza è obbligatorio")
    private String luogoPartenza;

    @NotBlank(message = "Il luogo di destinazione è obbligatorio")
    private String luogoDestinazione;

    @NotNull(message = "I km sono obbligatori")
    private Double km;

    private String trazione;
    private String orario;

    // I booleani hanno default false se non specificati
    private boolean sabato;
    private boolean domenica;
    private boolean sostaNotturna;
    private boolean facchinaggio;
    private boolean lavoroAggiuntivo;

    private String note;
}
