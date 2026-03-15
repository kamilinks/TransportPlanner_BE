package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

// Entità JPA per le descrizioni predefinite delle tratte (es. Carico/Scarico)
@Entity
@Table(name = "descrizioni_viaggio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.hibernate.annotations.SQLDelete(sql = "UPDATE descrizioni_viaggio SET deleted = true WHERE id=?")
@org.hibernate.annotations.SQLRestriction("deleted = false")
public class DescrizioneViaggio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codice;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean attivo;
}
