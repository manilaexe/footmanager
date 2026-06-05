package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.repository.UtenteRepository;
import it.footmanager.service.MessaggioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messaggi")
@RequiredArgsConstructor
public class MessaggioController {

    private final MessaggioService  messaggioService;
    private final UtenteRepository  utenteRepo;

    /** Posta in arrivo dell'utente autenticato */
    @GetMapping("/arrivo")
    public List<MessaggioDto> inArrivo(@AuthenticationPrincipal UserDetails ud) {
        Long uid = utenteRepo.findByUsername(ud.getUsername()).orElseThrow().getId();
        return messaggioService.inArrivo(uid);
    }

    /** Messaggi inviati dall'utente autenticato */
    @GetMapping("/inviati")
    public List<MessaggioDto> inviati(@AuthenticationPrincipal UserDetails ud) {
        Long uid = utenteRepo.findByUsername(ud.getUsername()).orElseThrow().getId();
        return messaggioService.inviati(uid);
    }

    /** Conteggio non letti */
    @GetMapping("/non-letti")
    public long nonLetti(@AuthenticationPrincipal UserDetails ud) {
        Long uid = utenteRepo.findByUsername(ud.getUsername()).orElseThrow().getId();
        return messaggioService.nonLetti(uid);
    }

    /** Invia un messaggio (solo STAFF, ALLENATORE, IT) */
    @PostMapping
    public ResponseEntity<MessaggioDto> invia(@Valid @RequestBody InviaMessaggioRequest req,
                                              @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messaggioService.invia(req, ud.getUsername()));
    }

    /** Segna un messaggio come letto */
    @PatchMapping("/{id}/letto")
    public MessaggioDto segnaLetto(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails ud) {
        return messaggioService.segnaLetto(id, ud.getUsername());
    }
}
