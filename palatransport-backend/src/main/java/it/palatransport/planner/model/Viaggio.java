package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

// Entità JPA centrale che rappresenta un viaggio e aggrega autista, veicoli e clienti
@Entity
@Table(name = "viaggi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.hibernate.annotations.SQLDelete(sql = "UPDATE viaggi SET deleted = true WHERE id=?")
@org.hibernate.annotations.SQLRestriction("deleted = false")
public class Viaggio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autista_id", nullable = false)
    private Autista autista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veicolo_salita_id")
    private Veicolo veicoloSalita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veicolo_discesa_id")
    private Veicolo veicoloDiscesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descrizione_salita_id")
    private DescrizioneViaggio descrizioneSalita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descrizione_discesa_id")
    private DescrizioneViaggio descrizioneDiscesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_salita_id")
    private Cliente clienteSalita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_discesa_id")
    private Cliente clienteDiscesa;

    @Column(nullable = false)
    private String luogoPartenza;

    @Column(nullable = false)
    private String luogoDestinazione;

    @Column(nullable = false)
    private Double km;

    @Column(nullable = false)
    private Double tariffaBase;

    @Column(nullable = false)
    private Double tariffaTotale;

    @Column
    private String trazione;

    @Column
    private String orario;

    @Column(nullable = false)
    private boolean sabato;

    @Column(nullable = false)
    private boolean domenica;

    @Column(nullable = false)
    private boolean sostaNotturna;

    @Column(nullable = false)
    private boolean facchinaggio;

    @Column(nullable = false)
    private boolean lavoroAggiuntivo;

    @Column(length = 1000)
    private String note;
}
