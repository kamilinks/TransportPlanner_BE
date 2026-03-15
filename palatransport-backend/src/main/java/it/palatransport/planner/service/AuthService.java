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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // Login utente
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole() != null ? user.getRole().name() : "USER")
                .build();

        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .build();
    }

    // Registrazione utente
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

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles(savedUser.getRole().name())
                .build();

        String token = jwtUtils.generateToken(userDetails);

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
