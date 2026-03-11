package it.palatransport.planner.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * UTILITY JWT: JwtUtils
 *
 * Questa classe è responsabile di tutte le operazioni sui token JWT:
 *   1. GENERATE: creare un token JWT dopo il login
 *   2. VALIDATE: verificare che un token sia valido e non scaduto
 *   3. EXTRACT: estrarre le informazioni (claims) da un token
 *
 * COS'È UN TOKEN JWT?
 * Un JWT è una stringa composta da 3 parti separate da punti:
 *   Header.Payload.Signature
 *   xxxxx.yyyyy.zzzzz
 *
 * - Header: algoritmo usato per la firma (es. HS256)
 * - Payload: "claims" = dati utente codificati in Base64 (username, scadenza, ecc.)
 * - Signature: firma digitale creata con la chiave segreta. Garantisce l'integrità.
 *
 * Quando il client manda un token, il server:
 *   1. Divide il token nelle 3 parti
 *   2. Ricalcola la firma con la propria chiave segreta
 *   3. Se corrisponde → token autentico. Se no → token falsificato/modificato.
 *
 * @Component → Spring crea automaticamente un'istanza di questa classe
 *   e la rende disponibile per l'iniezione (@Autowired o nel costruttore).
 */
@Component
public class JwtUtils {

    /**
     * @Value("${jwt.secret}") → Spring inietta il valore del campo "jwt.secret"
     * da application.properties. Questo è il modo corretto per leggere configurazioni:
     * non hardcodarle nel codice!
     */
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * GENERA un token JWT per un utente.
     * Chiamato dopo un login riuscito.
     *
     * @param userDetails → l'oggetto Spring Security che rappresenta l'utente autenticato
     * @return la stringa JWT da restituire al client
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token con claims extra (opzionale, per aggiungere dati personalizzati).
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                // "subject" = chi è l'utente. Di solito usiamo l'email come identificativo.
                .subject(userDetails.getUsername())
                // "issuedAt" = quando è stato emesso il token
                .issuedAt(new Date(System.currentTimeMillis()))
                // "expiration" = quando scade. jwtExpiration è in ms (es. 86400000 = 24h)
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                // "signWith" = firma il token con la nostra chiave segreta (algoritmo HS256)
                .signWith(getSigningKey())
                .compact(); // Genera la stringa JWT finale
    }

    /**
     * Estrae l'email (username/subject) dal token.
     * Usato dal filtro JWT per capire quale utente sta facendo la richiesta.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Verifica che il token sia valido:
     *   1. Username nel token = username nel database
     *   2. Token non scaduto
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /** Controlla se il token è scaduto */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** Estrae la data di scadenza dal token */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Metodo GENERICO per estrarre qualsiasi claim dal token.
     * Usa le "Function" di Java 8+ per essere flessibile:
     *   es. extractClaim(token, Claims::getSubject) → estrae il subject
     *       extractClaim(token, Claims::getExpiration) → estrae la scadenza
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parsifica il token JWT ed estrae TUTTI i claims (il payload).
     * Questo è il momento in cui viene verificata la firma del token.
     * Se la firma non è valida, JJWT lancia un'eccezione.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Converte la chiave segreta stringa in una chiave crittografica sicura.
     * La chiave viene codificata in Base64 e poi convertita in un oggetto SecretKey
     * usabile dall'algoritmo HMAC-SHA256.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
            java.util.Base64.getEncoder().encodeToString(secretKey.getBytes())
        );
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
