package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ JPA: Veicolo
 *
 * Mappa la tabella "veicoli" nel database.
 * Corrisponde al modello Angular: core/models/veicolo.model.ts
 *
 * In Angular il tipo era definito come 'salita' | 'discesa' (union type TypeScript).
 * In Java usiamo una @Enumerated che permette di fare la stessa cosa in modo sicuro.
 */
@Entity
@Table(name = "veicoli")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.SQLDelete(sql = "UPDATE veicoli SET deleted = true WHERE id=?")
@org.hibernate.annotations.SQLRestriction("deleted = false")
public class Veicolo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * La targa deve essere unica: non possono esserci due veicoli con la stessa targa.
     */
    @Column(nullable = false, unique = true)
    private String targa;

    /**
     * ENUM: usiamo un'enumerazione Java invece di una stringa libera.
     * Questo impedisce di salvare valori non validi (es. "salitaaaa").
     *
     * @Enumerated(EnumType.STRING) → Hibernate salva il nome dell'enum come stringa
     *   ("SALITA" o "DISCESA") invece che come numero intero (0 o 1).
     *   Preferire STRING rende il database leggibile anche senza codice.
     *
     * L'enum TipoVeicolo è definito qui sotto come classe interna.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeicolo tipo;

    /**
     * Rappresenta se il veicolo è un Trattore o un Semirimorchio.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipologia_mezzo")
    private TipologiaMezzo tipologiaMezzo;

    /**
     * "categoria" è nullable: nel modello Angular era definito come "categoria?",
     * con il punto interrogativo che significa "opzionale".
     */
    @Column
    private String categoria;

    /**
     * Enum annidato: definito come public static per poterlo usare anche fuori da Veicolo.
     * Corrisponde al tipo union 'salita' | 'discesa' in TypeScript.
     */
    public enum TipoVeicolo {
        SALITA,
        DISCESA
    }

    public enum TipologiaMezzo {
        TRATTORE,
        SEMIRIMORCHIO
    }
}
