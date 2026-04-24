package it.palatransport.planner.service;

import it.palatransport.planner.dto.AuthResponse;
import it.palatransport.planner.dto.LoginRequest;
import it.palatransport.planner.dto.RefreshTokenRequest;
import it.palatransport.planner.model.User;
import it.palatransport.planner.repository.UserRepository;
import it.palatransport.planner.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // Durata del Refresh Token (7 giorni) letta da application.properties
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    // Login utente: genera AccessToken JWT + RefreshToken UUID
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        UserDetails userDetails = buildUserDetails(user);
        String accessToken = jwtUtils.generateToken(userDetails);
        String refreshToken = generateAndSaveRefreshToken(user);

        return buildAuthResponse(accessToken, refreshToken, user);
    }

    // Registrazione utente: genera AccessToken JWT + RefreshToken UUID
    public AuthResponse register(it.palatransport.planner.dto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("L'email è già in uso.");
        }

        User newUser = User.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);
        UserDetails userDetails = buildUserDetails(savedUser);
        String accessToken = jwtUtils.generateToken(userDetails);
        String refreshToken = generateAndSaveRefreshToken(savedUser);

        return buildAuthResponse(accessToken, refreshToken, savedUser);
    }

    // Refresh: valida il Refresh Token ricevuto e genera un nuovo AccessToken
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // 1. Cerca l'utente tramite il Refresh Token ricevuto
        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token non valido"));

        // 2. Controlla che non sia scaduto
        if (user.getRefreshTokenExpiry().isBefore(Instant.now())) {
            // Invalida il refresh token scaduto nel DB
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
            userRepository.save(user);
            throw new RuntimeException("Refresh token scaduto. Effettua di nuovo il login.");
        }

        // 3. Genera un nuovo AccessToken
        UserDetails userDetails = buildUserDetails(user);
        String newAccessToken = jwtUtils.generateToken(userDetails);

        // 4. Ruota il Refresh Token (sicurezza: ogni refresh genera un nuovo RT)
        String newRefreshToken = generateAndSaveRefreshToken(user);

        return buildAuthResponse(newAccessToken, newRefreshToken, user);
    }

    // Logout: invalida il Refresh Token nel DB
    public void logout(String refreshToken) {
        userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
            user.setRefreshToken(null);
            user.setRefreshTokenExpiry(null);
            userRepository.save(user);
        });
    }

    // --- Metodi privati di supporto ---

    // Genera un UUID casuale come Refresh Token e lo persiste nel DB con scadenza
    private String generateAndSaveRefreshToken(User user) {
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(Instant.now().plusMillis(refreshTokenExpiration));
        userRepository.save(user);
        return refreshToken;
    }

    // Costruisce UserDetails da un'entità User (evita duplicazione di codice)
    private UserDetails buildUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }

    // Costruisce il DTO di risposta
    private AuthResponse buildAuthResponse(String accessToken, String refreshToken, User user) {
        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .email(user.getEmail())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }
}
