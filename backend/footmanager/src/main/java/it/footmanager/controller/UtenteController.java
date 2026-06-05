package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.service.UtenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping
    public List<UtenteDto> findAll() {
        return utenteService.findAll();
    }

    @GetMapping("/{id}")
    public UtenteDto findById(@PathVariable Long id) {
        return utenteService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('STAFF','IT')")
    public ResponseEntity<UtenteDto> crea(@Valid @RequestBody CreaUtenteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(utenteService.crea(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF','IT')")
    public UtenteDto aggiorna(@PathVariable Long id,
                              @RequestBody CreaUtenteRequest req) {
        return utenteService.aggiorna(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF','IT')")
    public ResponseEntity<Void> disattiva(@PathVariable Long id) {
        utenteService.disattiva(id);
        return ResponseEntity.noContent().build();
    }
}
