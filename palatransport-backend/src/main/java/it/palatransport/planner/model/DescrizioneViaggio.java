package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ JPA: DescrizioneViaggio
 *
 * Mappa la tabella "descrizioni_viaggio" nel database.
 * Corrisponde al modello Angular: core/models/descrizione-viaggio.model.ts
 *
 * Rappresenta le "etichette" descrittive per i viaggi: es. "Carico merce", "Consegna merce".
 * Sono prefissate dall'operatore e riutilizzate su più viaggi.
 */
@Entity
@Table(name = "descrizioni_viaggio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescrizioneViaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Codice breve della descrizione (es. "CARICO", "SCARICO").
     * Deve essere unico per evitare duplicati.
     */
    @Column(nullable = false, unique = true)
    private String codice;

    /**
     * Testo esteso della descrizione (es. "Carico merce al porto").
     */
    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean attivo;
}
