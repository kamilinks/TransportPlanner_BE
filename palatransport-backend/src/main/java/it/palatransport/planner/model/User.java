package it.palatransport.planner.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

// Entità JPA per la gestione degli utenti del sistema e dell'autenticazione
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    public enum Role {
        USER, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Refresh Token: valore UUID casuale, salvato in DB
    @Column(unique = true)
    private String refreshToken;

    // Data di scadenza del Refresh Token
    private Instant refreshTokenExpiry;
}
