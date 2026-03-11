package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * ENTITÀ JPA: TariffaConfig
 *
 * Mappa la tabella "tariffa_config" nel database.
 * Corrisponde al modello Angular: core/models/tariffa.model.ts
 *
 * Questa tabella contiene UN'UNICA RIGA: la configurazione delle tariffe
 * che l'amministratore può modificare dal pannello di controllo.
 * Viene letta ogni volta che si calcola una tariffa per un viaggio.
 *
 * Tutti i valori monetari sono in euro. Le maggiorazioni sono additive
 * (si aggiungono alla tariffa base calcolata in base ai km).
 */
@Entity
@Table(name = "tariffa_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TariffaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tariffa al km: prezzo per kilometro percorso (in euro).
     * Es: 1.15 → si paga 1,15 € per km
     */
    @Column(nullable = false)
    private Double tariffaBaseKm;

    /** Maggiorazione per il sabato (es. +50.00 €) */
    @Column(nullable = false)
    private Double maggiorazioneSabato;

    /** Maggiorazione per la domenica/festivo (es. +80.00 €) */
    @Column(nullable = false)
    private Double maggiorazioneDomenica;

    /** Maggiorazione per il trak "blue" (es. +30.00 €) */
    @Column(nullable = false)
    private Double maggiorazioneBlue;

    /** Maggiorazione per sosta notturna (es. +40.00 €) */
    @Column(nullable = false)
    private Double maggiorazioneSosta;

    /** Maggiorazione per facchinaggio (es. +25.00 €) */
    @Column(nullable = false)
    private Double maggiorazioneFacchinaggio;

    /** Maggiorazione per lavoro aggiuntivo (es. +35.00 €) */
    @Column(nullable = false)
    private Double maggiorazioneLavAgg;

    /**
     * Data e ora dell'ultima modifica alla configurazione tariffe.
     * Serve per sapere quando la configurazione è stata aggiornata l'ultima volta.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * @PrePersist e @PreUpdate sono callback JPA: vengono chiamati automaticamente
     * da Hibernate prima di salvare (persist) o prima di aggiornare (update) l'entità.
     * Li usiamo per aggiornare il campo updatedAt automaticamente.
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
