package it.footmanager.controller;

import it.footmanager.dto.AuthDto;
import it.footmanager.entity.Utente;
import it.footmanager.repository.UtenteRepository;
import it.footmanager.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/auth/login
 * Autentica l'utente e restituisce un JWT + il ruolo per il redirect del frontend.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService    userDetailsService;
    private final JwtUtils              jwtUtils;
    private final UtenteRepository      utenteRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(
            @Valid @RequestBody AuthDto.LoginRequest req) {

        // Autentica: lancia eccezione se credenziali sbagliate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        Utente utente = utenteRepository.findByUsername(req.getUsername()).orElseThrow();
        String nomeCompleto = utente.getNome() + " " + utente.getCognome();
        String ruolo = utente.getRuolo().getNome().name();

        return ResponseEntity.ok(new AuthDto.LoginResponse(token, ruolo, nomeCompleto, utente.getId()));
    }
}
