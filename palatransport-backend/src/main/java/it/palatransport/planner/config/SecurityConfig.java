package it.palatransport.planner.config;

import it.palatransport.planner.security.JwtAuthenticationFilter;
import it.palatransport.planner.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CONFIGURAZIONE SPRING SECURITY: SecurityConfig
 *
 * Questa classe è il "pannello di controllo" della sicurezza dell'applicazione.
 * Definisce:
 *   1. Quali endpoint sono PUBBLICI (accessibili senza autenticazione)
 *   2. Quali endpoint sono PROTETTI (richiedono un JWT valido)
 *   3. Come funziona l'autenticazione (stateless con JWT, no sessioni)
 *   4. La configurazione CORS (per permettere le chiamate da Angular)
 *   5. Come viene codificata la password (BCrypt)
 *
 * @Configuration → Dice a Spring che questa classe contiene Bean da configurare.
 * @EnableWebSecurity → Abilita la gestione della sicurezza Web di Spring.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * SECURITY FILTER CHAIN: la catena di filtri principale.
     *
     * Questo metodo configura:
     * - CSRF disabilitato: le API REST stateless non ne hanno bisogno
     *   (CSRF serve per le app con sessioni e form HTML tradizionali)
     * - CORS: configurato con corsConfigurationSource() qui sotto
     * - Regole di autorizzazione: chi può accedere a cosa
     * - Gestione sessione: STATELESS → nessuna sessione salvata lato server,
     *   ogni richiesta deve portare il suo token JWT
     * - Aggiunta del nostro filtro JWT prima del filtro standard di autenticazione
     *
     * @Bean → Il valore restituito viene registrato come Bean nel contesto Spring.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabilita CSRF (non necessario per REST API stateless)
            .csrf(AbstractHttpConfigurer::disable)

            // Configura CORS permettendo le chiamate da Angular
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Regole di autorizzazione
            .authorizeHttpRequests(auth -> auth
                // Endpoint PUBBLICI: accessibili senza JWT
                .requestMatchers("/api/auth/**").permitAll()  // Login
                .requestMatchers("/h2-console/**").permitAll() // Console H2 per sviluppo
                // Tutto il resto richiede autenticazione
                .anyRequest().authenticated()
            )

            // Headers per H2 console (necessario per il frame della UI H2)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

            // Gestione sessione: STATELESS = nessuna sessione HTTP sul server.
            // Spring Security non creerà cookie di sessione.
            // Ogni richiesta è indipendente e deve portare il suo JWT.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Usa il nostro AuthenticationProvider (che usa BCrypt + UserDetailsService)
            .authenticationProvider(authenticationProvider())

            // Inserisce il nostro filtro JWT PRIMA del filtro standard Spring Security.
            // Così il JWT viene verificato prima che Spring provi a fare autenticazione standard.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS CONFIGURATION: Cross-Origin Resource Sharing
     *
     * CORS è un meccanismo di sicurezza del browser che blocca le chiamate
     * HTTP tra domini diversi. Il browser "bloccherebbe" Angular su localhost:4200
     * che chiama il backend su localhost:8080 perché sono "origini" diverse.
     *
     * Qui diciamo esplicitamente al backend di ACCETTARE le chiamate da Angular.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origini permesse: il frontend Angular in sviluppo
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Metodi HTTP permessi
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Header permessi nelle richieste (Authorization per il JWT!)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Permette di inviare credenziali (cookie, Authorization header)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applica a tutti i path
        return source;
    }

    /**
     * AUTHENTICATION PROVIDER: il componente che gestisce la verifica delle credenziali.
     *
     * DaoAuthenticationProvider è l'implementazione standard di Spring Security
     * che usa un UserDetailsService (il nostro UserDetailsServiceImpl) e un
     * PasswordEncoder (BCrypt) per verificare le credenziali.
     *
     * Quando il login chiama authenticationManager.authenticate(),
     * questo provider:
     *   1. Carica l'utente tramite UserDetailsService.loadUserByUsername(email)
     *   2. Verifica che la password digitata corrisponda all'hash nel DB usando BCrypt
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * PASSWORD ENCODER: BCryptPasswordEncoder
     *
     * BCrypt è l'algoritmo di hashing delle password raccomandato.
     * Caratteristiche:
     *   - Include un "salt" casuale automaticamente (protegge dagli attacchi rainbow table)
     *   - È volutamente LENTO (circa 100ms) per rendere difficile il brute force
     *   - Ogni hash è diverso anche per la stessa password
     *   - Mai reversibile: non puoi ricavare la password dall'hash
     *
     * Uso: passwordEncoder().encode("myPassword") → "$2a$10$..."
     *      passwordEncoder().matches("myPassword", "$2a$10$...") → true
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AUTHENTICATION MANAGER: il componente centrale dell'autenticazione.
     *
     * Viene usato nel AuthController per gestire il login.
     * Spring lo configura automaticamente in base al provider registrato.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
