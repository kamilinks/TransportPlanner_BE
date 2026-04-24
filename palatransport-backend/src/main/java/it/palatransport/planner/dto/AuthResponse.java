package it.palatransport.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: AuthResponse
 *
 * La risposta JSON che il backend invia dopo un login riuscito:
 *   {
 *     "token": "eyJhbGciOiJIUzI1NiJ9...",
 *     "id": 1,
 *     "email": "mirko@mirko.it",
 *     "nome": "Mirko",
 *     "cognome": "Mameli"
 *   }
 *
 * Il frontend Angular dovrà:
 *   1. Salvare il "token" in localStorage (o un cookie sicuro)
 *   2. Includere il token in ogni richiesta successiva:
 *      headers: { Authorization: 'Bearer ' + token }
 *
 * NOTA: Non includiamo la password! I DTO di risposta non espongono mai dati sensibili.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;           // AccessToken JWT (breve durata: 15 min)
    private String refreshToken;    // RefreshToken UUID (lunga durata: 7 giorni)
    private Long id;
    private String email;
    private String nome;
    private String cognome;
    private String role;
}
