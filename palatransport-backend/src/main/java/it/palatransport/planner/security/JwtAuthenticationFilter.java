package it.palatransport.planner.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * FILTRO JWT: JwtAuthenticationFilter
 *
 * Questo è il "guardiano" del backend. Viene eseguito UNA VOLTA per ogni
 * richiesta HTTP che arriva al server (estende OncePerRequestFilter).
 *
 * Il suo compito è:
 *   1. Leggere l'header "Authorization" dalla richiesta HTTP
 *   2. Estrarre il token JWT (se presente)
 *   3. Validarlo
 *   4. Se valido → "autenticare" l'utente nel SecurityContext di Spring
 *
 * Il SecurityContext è una specie di "memoria di sessione per richiesta":
 * una volta impostato, Spring Security sa che l'utente è autenticato e lo
 * lascia passare verso il Controller.
 *
 * Schema del flusso:
 * [Richiesta HTTP] → [JwtAuthenticationFilter] → [Controller]
 *     con header:           valida JWT              protetto
 *     Authorization:        imposta Context
 *     Bearer <token>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Leggi l'header "Authorization" dalla richiesta
        final String authHeader = request.getHeader("Authorization");

        // 2. Se l'header non esiste o non inizia con "Bearer ", passa al prossimo filtro
        //    senza autenticare (la richiesta sarà anonima, o bloccata dal SecurityConfig)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Estrai il token rimuovendo il prefisso "Bearer " (7 caratteri)
        final String jwt = authHeader.substring(7);

        // 4. Estrai l'email (username) dal token
        final String userEmail;
        try {
            userEmail = jwtUtils.extractUsername(jwt);
        } catch (Exception e) {
            // Token malformato o con firma invalida → passa senza autenticare
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Se abbiamo un'email E non è già autenticato nel contesto corrente
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Carica i dettagli dell'utente dal database
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // 7. Verifica che il token sia valido per questo utente
            if (jwtUtils.isTokenValid(jwt, userDetails)) {

                // 8. Crea un token di autenticazione Spring Security
                //    UsernamePasswordAuthenticationToken è la classe che Spring Security
                //    usa per rappresentare un utente autenticato nel contesto.
                //    - Primo arg: l'utente (userDetails)
                //    - Secondo arg: le credenziali (null: già verificate via JWT)
                //    - Terzo arg: i permessi/ruoli dell'utente
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 9. Aggiungi dettagli sulla richiesta HTTP all'autenticazione
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. Imposta l'autenticazione nel SecurityContext
                //     Da questo momento Spring Security considera la richiesta autenticata.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Continua la catena dei filtri (passa al Controller)
        filterChain.doFilter(request, response);
    }
}
