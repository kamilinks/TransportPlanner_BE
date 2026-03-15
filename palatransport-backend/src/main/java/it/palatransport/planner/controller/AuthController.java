package it.palatransport.planner.controller;

import it.palatransport.planner.dto.AuthResponse;
import it.palatransport.planner.dto.LoginRequest;
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

    // Login utente e rilascio JWT
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Registrazione nuovo utente
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody it.palatransport.planner.dto.RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}
