package it.palatransport.planner.service;

import it.palatransport.planner.dto.AuthResponse;
import it.palatransport.planner.dto.LoginRequest;
import it.palatransport.planner.model.User;
import it.palatransport.planner.repository.UserRepository;
import it.palatransport.planner.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * SERVICE: AuthService
 *
 * Contiene la logica di business per l'autenticazione.
 *
 * LAYER ARCHITECTURE: nel pattern MVC/3-tier che stiamo usando:
 *   Controller → riceve la richiesta HTTP e la delega al Service
 *   Service    → contiene la LOGICA DI BUSINESS (questa classe!)
 *   Repository → parla con il database
 *
 * Il Service non deve sapere nulla di HTTP (no HttpRequest, no headers).
 * Il Controller non deve sapere nulla di business logic.
 * Questa separazione rende il codice testabile, modulare e manutenibile.
 *
 * @Service → Spring crea un'istanza unica (Singleton) di questa classe
 *   e la inietta dove viene richiesta (es. nei Controller).
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * METODO LOGIN: autentica un utente e ritorna un token JWT.
     *
     * FLUSSO:
     * 1. authenticationManager.authenticate() → delega a Spring Security la verifica:
     *    a. carica l'utente da DB (via UserDetailsService)
     *    b. verifica la password con BCrypt
     *    c. Se errata → lancia BadCredentialsException (Spring risponde 401)
     *
     * 2. Se l'autenticazione va a buon fine, carica l'utente dal DB
     *    per includere i suoi dati nella risposta.
     *
     * 3. Genera il token JWT tramite JwtUtils.
     *
     * 4. Costruisce e ritorna AuthResponse con token + dati utente.
     *
     * @param request contiene email e password dal corpo della richiesta HTTP
     * @return AuthResponse con il token JWT e i dati dell'utente
     */
    public AuthResponse login(LoginRequest request) {
        // Step 1: Autentica con Spring Security (verifica email + password BCrypt)
        // Se le credenziali sono errate, Spring lancia BadCredentialsException
        // e il framework risponde automaticamente con HTTP 401.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Step 2: Carica l'utente dal database (a questo punto sappiamo che esiste)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Step 3: Crea un oggetto UserDetails per generare il token
        // (JwtUtils si aspetta un UserDetails, non la nostra Entity User)
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole() != null ? user.getRole().name() : "USER")
                .build();

        // Step 4: Genera il token JWT
        String token = jwtUtils.generateToken(userDetails);

        // Step 5: Costruisce e ritorna la risposta con il pattern Builder di Lombok
        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }

    /**
     * METODO REGISTER: crea un nuovo utente e lo logga in automatico.
     *
     * FLUSSO:
     * 1. Controlla se l'email è già in uso.
     * 2. Crea il nuovo User e hasha la password con BCrypt.
     * 3. Salva l'utente nel Database.
     * 4. Genera il token JWT.
     * 5. Restituisce AuthResponse (stesso formato del login, per consentire il login immediato).
     *
     * @param request Dati di registrazione dal DTO
     * @return AuthResponse con token JWT
     */
    public AuthResponse register(it.palatransport.planner.dto.RegisterRequest request) {
        // Step 1: Controllo email duplicata
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("L'email è già in uso.");
        }

        // Step 2: Crea ed hasha la password del nuovo utente
        User newUser = User.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                // Hashing della password prima del salvataggio! (MOLTO IMPORTANTE)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER) // Default role
                .build();

        // Step 3: Salva nel database
        User savedUser = userRepository.save(newUser);

        // Step 4: Usa lo stesso processo del login per generare il token
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles(savedUser.getRole().name())
                .build();

        String token = jwtUtils.generateToken(userDetails);

        // Step 5: Costruisce la risposta
        return AuthResponse.builder()
                .token(token)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .nome(savedUser.getNome())
                .cognome(savedUser.getCognome())
                .role(savedUser.getRole().name())
                .build();
    }
}
