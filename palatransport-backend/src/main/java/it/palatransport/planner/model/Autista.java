package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

// Entità JPA per la gestione degli autisti e trazionisti
@Entity
@Table(name = "autisti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.hibernate.annotations.SQLDelete(sql = "UPDATE autisti SET deleted = true WHERE id=?")
@org.hibernate.annotations.SQLRestriction("deleted = false")
public class Autista extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean attivo;

    @Column(name = "is_trazionista", nullable = false, columnDefinition = "boolean default false")
    private boolean trazionista;

    @Column(name = "tariffa_km")
    private Double tariffaKm;
}
