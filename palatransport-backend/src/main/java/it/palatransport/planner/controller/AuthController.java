package it.palatransport.planner.controller;

import it.palatransport.planner.dto.AuthResponse;
import it.palatransport.planner.dto.LoginRequest;
import it.palatransport.planner.dto.RefreshTokenRequest;
import it.palatransport.planner.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller autenticazione
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Login utente: restituisce AccessToken JWT + RefreshToken UUID
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Registrazione nuovo utente
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody it.palatransport.planner.dto.RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Rinnovo AccessToken: riceve il RefreshToken e restituisce un nuovo AccessToken + nuovo RefreshToken
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    // Logout: invalida il RefreshToken nel DB
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
