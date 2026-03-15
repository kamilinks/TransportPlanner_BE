package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String targa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeicolo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipologia_mezzo")
    private TipologiaMezzo tipologiaMezzo;

    @Column
    private String categoria;

    public enum TipoVeicolo {
        SALITA,
        DISCESA
    }

    public enum TipologiaMezzo {
        TRATTORE,
        SEMIRIMORCHIO
    }
}
