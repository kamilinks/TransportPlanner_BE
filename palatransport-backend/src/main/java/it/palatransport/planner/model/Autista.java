package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ JPA: Autista
 *
 * Mappa la tabella "autisti" nel database.
 * Corrisponde al modello Angular: core/models/autista.model.ts
 *
 * Questa è l'entità più semplice del dominio: solo dati anagrafici di un autista.
 */
@Entity
@Table(name = "autisti")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Autista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(nullable = false) → Il nome dell'autista è obbligatorio.
     * Se si tenta di salvare un Autista senza nome, il database (e Hibernate)
     * lancerà un'eccezione.
     */
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    /**
     * Un campo boolean senza @Column usa i valori default:
     * - nullable = true (ma essendo boolean primitivo, non può essere null)
     * Per sicurezza lo mappiamo esplicitamente con columnDefinition.
     * "attivo = false" di default → un autista appena creato non è ancora attivo
     * finché non viene confermato.
     */
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean attivo;
}
