package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trazionisti")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trazionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String partitaIva;

    private String indirizzo;

    private String telefono;

    private String email;
}
