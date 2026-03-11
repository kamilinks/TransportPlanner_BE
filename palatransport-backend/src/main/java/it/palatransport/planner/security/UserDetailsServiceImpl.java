package it.palatransport.planner.security;

import it.palatransport.planner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * SERVIZIO: UserDetailsServiceImpl
 *
 * Spring Security, quando deve autenticare un utente, NON sa dove sono salvati
 * i dati (database, LDAP, file, ecc). Deve CHIEDERE a qualcuno.
 *
 * Quel "qualcuno" è questa classe: implementa l'interfaccia UserDetailsService
 * di Spring Security. Quando Spring Security ha bisogno di caricare un utente
 * (es. per verificare un token JWT), chiama il metodo loadUserByUsername().
 *
 * In questo metodo noi usiamo il UserRepository per cercare l'utente nel DB.
 * Se non esiste, lanciamo UsernameNotFoundException.
 *
 * @Service → Spring crea questa classe come Bean (componente gestito) nel contesto.
 * @RequiredArgsConstructor → Lombok genera il costruttore con tutti i campi "final".
 *   Questo equivale a scrivere:
 *   public UserDetailsServiceImpl(UserRepository userRepository) {
 *       this.userRepository = userRepository;
 *   }
 *   È il modo corretto di fare "Dependency Injection" in Spring.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carica l'utente dal database dato il suo "username".
     * Nel nostro caso il "username" è l'email.
     *
     * Spring Security.User.withUsername(...) crea un oggetto UserDetails
     * standard di Spring Security, che contiene:
     *   - username (l'email dell'utente)
     *   - password (l'hash BCrypt salvato nel DB)
     *   - roles/authorities (i permessi). Per ora tutti gli utenti hanno il ruolo "USER".
     *
     * @throws UsernameNotFoundException se l'utente non esiste nel DB.
     *    Spring Security cattura questa eccezione e risponde con 401 Unauthorized.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utente non trovato con email: " + username)
                );
    }
}
