package it.palatransport.planner.controller;

import it.palatransport.planner.dto.AuthResponse;
import it.palatransport.planner.dto.LoginRequest;
import it.palatransport.planner.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLER: AuthController
 *
 * Il Controller è il "cameriere" dell'applicazione: riceve le richieste HTTP
 * dall'esterno, le passa al Service per elaborarle, e restituisce la risposta.
 *
 * ANNOTAZIONI DI CLASSE:
 *
 * @RestController → Combina @Controller + @ResponseBody.
 *   Significa: "questa classe gestisce richieste HTTP e ogni metodo
 *   restituisce direttamente un oggetto Java che Spring serializza
 *   automaticamente in JSON (grazie a Jackson incluso in spring-boot-starter-web)".
 *
 * @RequestMapping("/api/auth") → Prefisso comune per tutti gli endpoint di questo controller.
 *   Tutti gli URL inizieranno con /api/auth/...
 *
 * @CrossOrigin → Permette le richieste da altri domini (Angular su localhost:4200).
 *   NOTA: abbiamo già configurato CORS globalmente in SecurityConfig,
 *   ma è buona pratica anche qui come documentazione degli endpoint.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * ENDPOINT: POST /api/auth/login
     *
     * Questo è l'unico endpoint PUBBLICO (non richiede JWT, come configurato in SecurityConfig).
     * Riceve le credenziali e restituisce il token JWT.
     *
     * @PostMapping → Mappa questo metodo alle richieste HTTP POST su /api/auth/login
     *
     * @RequestBody → Dice a Spring di leggere il corpo (body) della richiesta HTTP
     *   e deserializzarlo automaticamente in un oggetto LoginRequest.
     *   Il client invia JSON: { "email": "...", "password": "..." }
     *
     * @Valid → Attiva la validazione Jakarta Validation sull'oggetto LoginRequest.
     *   Se i campi @NotBlank, @Email, ecc. non sono soddisfatti,
     *   Spring risponde automaticamente con HTTP 400 Bad Request.
     *
     * ResponseEntity<T> → Risposta HTTP completa: puoi specificare body, status code, headers.
     *   ResponseEntity.ok(body) → 200 OK con il body nella risposta
     *   ResponseEntity.status(401).build() → 401 Unauthorized senza body
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * ENDPOINT: POST /api/auth/register
     *
     * Riceve i dati di registrazione, crea l'utente e restituisce il token JWT
     * per il login automatico.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody it.palatransport.planner.dto.RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}
