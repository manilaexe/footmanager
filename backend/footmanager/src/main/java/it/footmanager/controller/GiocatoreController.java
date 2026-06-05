package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.service.GiocatoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/giocatori")
@RequiredArgsConstructor
public class GiocatoreController {

    private final GiocatoreService giocatoreService;

    @GetMapping("/squadra/{squadraId}")
    public List<GiocatoreDto> bySquadra(@PathVariable Long squadraId) {
        return giocatoreService.findBySquadra(squadraId);
    }

    @GetMapping("/{id}")
    public GiocatoreDto findById(@PathVariable Long id) {
        return giocatoreService.findById(id);
    }

    @GetMapping("/squadra/{squadraId}/top-marcatori")
    public List<GiocatoreDto> topMarcatori(@PathVariable Long squadraId) {
        return giocatoreService.topMarcatori(squadraId);
    }
}
