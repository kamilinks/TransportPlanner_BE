package it.palatransport.planner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GESTORE GLOBALE ECCEZIONI: GlobalExceptionHandler
 *
 * Senza questa classe, quando il codice lancia un'eccezione (es. RuntimeException)
 * Spring risponde con una pagina di errore HTML generica e poco utile.
 *
 * Con @RestControllerAdvice, possiamo intercettare le eccezioni da TUTTI i controller
 * in un unico posto e formattare risposte JSON chiare per il client:
 *   {
 *     "timestamp": "2024-01-15T10:30:00",
 *     "status": 404,
 *     "error": "Not Found",
 *     "message": "Autista non trovato con id: 99"
 *   }
 *
 * @RestControllerAdvice → Combina @ControllerAdvice (si applica a tutti i controller)
 *   + @ResponseBody (converte la risposta in JSON).
 *
 * @ExceptionHandler(XxxException.class) → Dice a Spring: "quando viene lanciata
 *   questa specifica eccezione, chiama questo metodo per gestirla".
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce i RuntimeException generici (es. "Autista non trovato con id: X").
     * Li mappa a HTTP 404 Not Found.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Gestisce le eccezioni di autenticazione fallita (credenziali errate al login).
     * Le mappa a HTTP 401 Unauthorized.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Credenziali non valide");
    }

    /**
     * Gestisce gli errori di validazione (@Valid nei Controller).
     * Es: email mancante, km null, ecc.
     * Mappa a HTTP 400 Bad Request con la lista di tutti i campi invalidi.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Raccoglie tutti i messaggi di errore di validazione
        String errori = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errori);
    }

    /**
     * Crea un corpo JSON uniforme per tutti gli errori.
     * Avere un formato coerente aiuta il frontend a gestire gli errori facilmente.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
