package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ JPA: Cliente
 * 
 * Rappresenta un cliente (Blue, Intermodal, Pinna, ecc.)
 */
@Entity
@Table(name = "clienti")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String ragioneSociale;

    @Column(length = 11)
    private String partitaIva;

    @Column
    private String indirizzo;

    @Column
    private String citta;

    @Column(length = 5)
    private String cap;

    @Column
    private String telefono;

    @Column
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private boolean attivo = true;
}
