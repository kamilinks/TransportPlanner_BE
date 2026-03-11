package it.palatransport.planner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO: LoginRequest
 *
 * Un DTO (Data Transfer Object) è un oggetto "stupido" che serve SOLO
 * a trasportare dati da un posto all'altro. Non ha logica di business.
 *
 * Questo DTO rappresenta il corpo (body) JSON della richiesta di login:
 *   {
 *     "email": "mirko@mirko.it",
 *     "password": "11"
 *   }
 *
 * PERCHÉ usare DTO invece dell'Entity direttamente?
 *   1. SICUREZZA: non esponi campi sensibili (es. id, password hashata)
 *   2. VALIDAZIONE: puoi aggiungere regole di validazione (@NotBlank, @Email)
 *   3. DISACCOPPIAMENTO: l'API può cambiare struttura senza toccare il database
 *   4. CHIAREZZA: è esplicito cosa si aspetta il client e cosa il server risponde
 *
 * ANNOTAZIONI DI VALIDAZIONE (spring-boot-starter-validation):
 * @NotBlank → il campo non può essere null NÉ una stringa vuota ("  ")
 * @Email    → il campo deve avere il formato di un'email valida
 *
 * Queste vengono verificate nei Controller con @Valid o @Validated.
 */
@Data
public class LoginRequest {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    private String password;
}
