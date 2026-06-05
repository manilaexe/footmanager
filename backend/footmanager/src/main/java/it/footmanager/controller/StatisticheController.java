package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.service.GiocatoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistiche")
@RequiredArgsConstructor
public class StatisticheController {

    private final GiocatoreService giocatoreService;

    @GetMapping("/giocatore/{giocatoreId}")
    public StatisticheDto getStatistiche(@PathVariable Long giocatoreId) {
        return giocatoreService.getStatistiche(giocatoreId);
    }

    @PutMapping("/giocatore/{giocatoreId}")
    @PreAuthorize("hasAnyRole('STAFF','ALLENATORE','IT')")
    public StatisticheDto aggiorna(@PathVariable Long giocatoreId,
                                   @RequestBody AggiornaStatisticheRequest req) {
        return giocatoreService.aggiornaStatistiche(giocatoreId, req);
    }
}
