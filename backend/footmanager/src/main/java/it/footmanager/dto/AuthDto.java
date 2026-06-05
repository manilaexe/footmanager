package it.footmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

// ── Request ──────────────────────────────────────────────────────────────
public class AuthDto {

    @Data
    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
    }

    // ── Response ─────────────────────────────────────────────────────────
    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String ruolo;
        private String nomeCompleto;
        private Long   utenteId;
    }
}
