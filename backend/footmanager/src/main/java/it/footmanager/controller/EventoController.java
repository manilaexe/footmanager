package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping("/squadra/{squadraId}")
    public List<EventoDto> bySquadra(@PathVariable Long squadraId) {
        return eventoService.findBySquadra(squadraId);
    }

    /** Filtra per mese: /api/eventi/squadra/1/mese?anno=2025&mese=5 */
    @GetMapping("/squadra/{squadraId}/mese")
    public List<EventoDto> byMese(@PathVariable Long squadraId,
                                  @RequestParam int anno,
                                  @RequestParam int mese) {
        return eventoService.findByMese(squadraId, anno, mese);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('STAFF','ALLENATORE','IT')")
    public ResponseEntity<EventoDto> crea(@Valid @RequestBody CreaEventoRequest req,
                                          @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventoService.crea(req, ud.getUsername()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF','ALLENATORE','IT')")
    public EventoDto aggiorna(@PathVariable Long id,
                              @Valid @RequestBody CreaEventoRequest req) {
        return eventoService.aggiorna(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF','ALLENATORE','IT')")
    public ResponseEntity<Void> elimina(@PathVariable Long id) {
        eventoService.elimina(id);
        return ResponseEntity.noContent().build();
    }
}
