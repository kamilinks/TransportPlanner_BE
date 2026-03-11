package it.palatransport.planner.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO: ViaggioResponse
 *
 * Il JSON che il backend restituisce quando si richiede un viaggio.
 * A differenza dell'Entity Viaggio (che ha riferimenti agli oggetti completi),
 * qui "appiattimo" la struttura per il frontend:
 *   - invece di inviare l'intero oggetto Autista, inviamo i singoli campi
 *     autistaId, autistaNome, autistaConome → Angular li sa già rappresentare
 *
 * Questo "appiattimento" viene detto "projection" o "flattening" ed è
 * responsabilità del Service (non del Controller, non dell'Entity).
 *
 * Corrisponde 1:1 all'interfaccia Viaggio in Angular (viaggio.model.ts).
 */
@Data
@Builder
public class ViaggioResponse {
    private Long id;
    private LocalDate data;

    // Dati autista (appiattiti dall'oggetto Autista)
    private Long autistaId;
    private String autistaNome;
    private String autistaCognome;

    // Dati veicolo salita (appiattiti)
    private Long veicoloSalitaId;
    private String targaSalita;

    // Dati veicolo discesa (appiattiti)
    private Long veicoloDiscesaId;
    private String targaDiscesa;

    // Dati descrizione salita (appiattiti)
    private Long descrizioneSalitaId;
    private String descrizioneSalita;

    // Dati descrizione discesa (appiattiti)
    private Long descrizioneDiscesaId;
    private String descrizioneDiscesa;

    private String luogoPartenza;
    private String luogoDestinazione;
    private Double km;
    private Double tariffaBase;
    private Double tariffaTotale;
    private String trazione;
    private String orario;
    private boolean isSabato;
    private boolean isDomenica;
    private boolean sostaNotturna;
    private boolean facchinaggio;
    private boolean lavoroAggiuntivo;
    private String note;
}
