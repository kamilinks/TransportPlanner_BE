package it.palatransport.planner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// DTO: richiesta di refresh del token JWT
// Il frontend invia questo oggetto quando l'AccessToken è scaduto
@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Il refresh token non può essere vuoto")
    private String refreshToken;
}
