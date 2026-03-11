package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ JPA: User
 *
 * Un'entità JPA è una classe Java "normale" arricchita con annotazioni speciali
 * che dicono a Hibernate come mapparla su una tabella del database.
 *
 * Questa classe rappresenta gli utenti che possono accedere al sistema.
 * Corrisponde al modello Angular: core/models/user.model.ts
 *
 * ---
 * ANNOTAZIONI DI CLASSE:
 *
 * @Entity → Dice a Hibernate: "Questa classe è una tabella del database".
 *            Hibernate la troverà automaticamente e creerà la tabella.
 *
 * @Table(name = "users") → Specifica il nome della tabella nel DB.
 *            Il nome "users" è scelto perché "user" è una parola riservata in SQL.
 *
 * ANNOTAZIONI LOMBOK (generano codice automaticamente):
 * @Data     → Genera getter, setter, toString(), equals(), hashCode()
 * @Builder  → Permette di costruire oggetti con un pattern fluente:
 *             User user = User.builder().email("a@b.it").nome("Mario").build();
 * @NoArgsConstructor → Genera il costruttore senza argomenti (richiesto da JPA)
 * @AllArgsConstructor → Genera il costruttore con tutti gli argomenti (richiesto da @Builder)
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * @Id → Questa è la chiave primaria della tabella (campo univoco identificativo).
     * @GeneratedValue(strategy = GenerationType.IDENTITY) → Il database genera automaticamente
     *         l'ID incrementando un contatore (1, 2, 3, ...). Non dobbiamo impostarlo noi.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(unique = true, nullable = false) → Vincoli sulla colonna:
     *   - unique: non possono esistere due utenti con la stessa email
     *   - nullable = false: il campo non può essere NULL nel database
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * La password viene salvata HASHATA (non in chiaro) grazie a BCrypt.
     * BCrypt è un algoritmo di hashing a senso unico: non puoi "decifrare" la password,
     * ma puoi confrontare una password digitata con il suo hash salvato.
     */
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;
}
