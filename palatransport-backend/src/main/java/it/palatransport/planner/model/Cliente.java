package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

// Entità JPA che rappresenta un cliente dell'azienda
@Entity
@Table(name = "clienti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.hibernate.annotations.SQLDelete(sql = "UPDATE clienti SET deleted = true WHERE id=?")
@org.hibernate.annotations.SQLRestriction("deleted = false")
public class Cliente extends BaseEntity {

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
