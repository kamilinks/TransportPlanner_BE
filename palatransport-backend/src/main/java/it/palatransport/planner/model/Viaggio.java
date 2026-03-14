package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * ENTITÀ JPA: Viaggio — L'ENTITÀ CENTRALE DEL DOMINIO
 *
 * Mappa la tabella "viaggi" nel database.
 * Corrisponde al modello Angular: core/models/viaggio.model.ts
 *
 * Questa è l'entità più complessa perché aggrega tutte le altre tramite
 * RELAZIONI (@ManyToOne). Vediamo cosa significa:
 *
 * @ManyToOne → "MOLTI a UNO": molti Viaggi appartengono a UN Autista.
 *   - @JoinColumn(name = "autista_id") → specifica il nome della colonna
 *     "chiave esterna" (Foreign Key) nella tabella "viaggi" che punta
 *     all'ID dell'autista nella tabella "autisti".
 *
 * Questo è il cuore delle relazioni in JPA: invece di salvare solo un ID
 * (come faceva Angular con autistaId: number), JPA salva un riferimento
 * all'oggetto Autista completo, e gestisce il join automaticamente.
 *
 * Quando leggi un Viaggio, puoi accedere direttamente a viaggio.getAutista().getNome()
 * senza dover fare query separate — JPA le fa per te!
 */
@Entity
@Table(name = "viaggi")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Viaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * La data del viaggio. Usiamo LocalDate (solo data, senza ora)
     * perché in Angular il campo data era un Date senza fuso orario.
     */
    @Column(nullable = false)
    private LocalDate data;

    /**
     * RELAZIONE ManyToOne: Autista
     *
     * Un viaggio ha UN autista. Un autista può avere MOLTI viaggi.
     * Sul DB questo si traduce in una colonna "autista_id" nella tabella "viaggi"
     * che contiene l'ID dell'autista corrispondente nella tabella "autisti".
     *
     * fetch = FetchType.LAZY → JPA caricherà l'autista solo quando lo richiediamo
     *   (lazy = pigro). Migliora le performance evitando join inutili.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autista_id", nullable = false)
    private Autista autista;

    /**
     * Veicolo usato per la SALITA (tratta di andata).
     * Può essere null: non tutti i viaggi hanno un veicolo per la salita distinto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veicolo_salita_id")
    private Veicolo veicoloSalita;

    /**
     * Veicolo usato per la DISCESA (tratta di ritorno).
     * Può essere null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veicolo_discesa_id")
    private Veicolo veicoloDiscesa;

    /**
     * Descrizione della tratta di SALITA (es. "Carico merce").
     * Può essere null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descrizione_salita_id")
    private DescrizioneViaggio descrizioneSalita;

    /**
     * Descrizione della tratta di DISCESA (es. "Consegna merce").
     * Può essere null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descrizione_discesa_id")
    private DescrizioneViaggio descrizioneDiscesa;

    /**
     * Cliente che paga o richiede la tratta di SALITA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_salita_id")
    private Cliente clienteSalita;

    /**
     * Cliente che paga o richiede la tratta di DISCESA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_discesa_id")
    private Cliente clienteDiscesa;

    // --- DATI DEL VIAGGIO ---

    @Column(nullable = false)
    private String luogoPartenza;

    @Column(nullable = false)
    private String luogoDestinazione;

    /** Chilometri percorsi nel viaggio */
    @Column(nullable = false)
    private Double km;

    /** Tariffa calcolata in base ai km (km * tariffaBaseKm dalla configurazione) */
    @Column(nullable = false)
    private Double tariffaBase;

    /** Tariffa finale con tutte le maggiorazioni applicate */
    @Column(nullable = false)
    private Double tariffaTotale;

    /**
     * Tipo di trazione: es. "BLUE", "RED", "GREEN".
     * Rimane una stringa libera (non enum) per flessibilità.
     * Può essere null.
     */
    @Column
    private String trazione;

    /**
     * Fascia oraria: "matt" (mattino), "pom" (pomeriggio), "notte".
     * Può essere null.
     */
    @Column
    private String orario;

    // --- FLAGS BOOLEANI: maggiorazioni speciali ---

    /** Il viaggio cade di sabato? */
    @Column(nullable = false)
    private boolean sabato;

    /** Il viaggio cade di domenica? */
    @Column(nullable = false)
    private boolean domenica;

    /** È prevista una sosta notturna? */
    @Column(nullable = false)
    private boolean sostaNotturna;

    /** È previsto facchinaggio? */
    @Column(nullable = false)
    private boolean facchinaggio;

    /** C'è del lavoro aggiuntivo non standard? */
    @Column(nullable = false)
    private boolean lavoroAggiuntivo;

    /** Note libere sull'autista o sul viaggio */
    @Column(length = 1000)
    private String note;
}
