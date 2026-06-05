package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.*;
import it.footmanager.exception.ResourceNotFoundException;
import it.footmanager.repository.*;
import it.footmanager.service.GiocatoreService;
import lombok.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint aggregati per le dashboard del frontend.
 * Ogni ruolo riceve solo i dati di propria competenza.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UtenteRepository      utenteRepo;
    private final GiocatoreRepository   giocatoreRepo;
    private final MessaggioRepository   mesRepo;
    private final EventoRepository      eventoRepo;
    private final StatisticheRepository statRepo;
    private final GiocatoreService      giocatoreService;

    // ── Dashboard giocatore ───────────────────────────────────────────────
    @GetMapping("/giocatore")
    public GiocatoreDashboard giocatore(@AuthenticationPrincipal UserDetails ud) {
        Utente utente = utenteRepo.findByUsername(ud.getUsername()).orElseThrow();
        Giocatore g = giocatoreRepo.findByUtente_Id(utente.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Giocatore per " + ud.getUsername()));

        StatisticheDto stat   = giocatoreService.getStatistiche(g.getId());
        long nonLetti         = mesRepo.countByDestinatario_IdAndLettoIlIsNull(utente.getId());
        List<EventoDto> eventi = eventoRepo
                .findBySquadra_IdOrderByDataInizioAsc(g.getSquadra().getId())
                .stream().limit(5).map(e -> EventoDto.builder()
                        .id(e.getId()).titolo(e.getTitolo()).tipo(e.getTipo())
                        .dataInizio(e.getDataInizio()).luogo(e.getLuogo()).build())
                .toList();

        return new GiocatoreDashboard(
                giocatoreService.toDto(g), stat, (int) nonLetti, eventi);
    }

    // ── Dashboard dirigenza ───────────────────────────────────────────────
    @GetMapping("/dirigenza/{squadraId}")
    public DirigenzaDashboard dirigenza(@PathVariable Long squadraId) {
        List<Giocatore> rosa  = giocatoreRepo.findBySquadra_Id(squadraId);
        List<GiocatoreDto> topMarcatori = giocatoreService.topMarcatori(squadraId)
                .stream().limit(5).toList();

        // Medie squadra semplificate
        double mediaGol   = rosa.stream()
                .mapToInt(g -> g.getStatistiche() != null ? g.getStatistiche().getGol() : 0)
                .average().orElse(0);
        long totEventi = eventoRepo.findBySquadra_IdOrderByDataInizioAsc(squadraId).size();

        return new DirigenzaDashboard(rosa.size(), topMarcatori, totEventi, mediaGol);
    }

    // ── Record di risposta ────────────────────────────────────────────────
    @Data @AllArgsConstructor
    public static class GiocatoreDashboard {
        private GiocatoreDto    profilo;
        private StatisticheDto  statistiche;
        private int             messaggiNonLetti;
        private List<EventoDto> prossimiEventi;
    }

    @Data @AllArgsConstructor
    public static class DirigenzaDashboard {
        private int                  numGiocatori;
        private List<GiocatoreDto>   topMarcatori;
        private long                 totEventi;
        private double               mediaGol;
    }
}
