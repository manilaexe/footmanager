package it.footmanager.service;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.*;
import it.footmanager.exception.*;
import it.footmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiocatoreService {

    private final GiocatoreRepository   giocatoreRepo;
    private final StatisticheRepository statisticheRepo;

    // ── Rosa squadra ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<GiocatoreDto> findBySquadra(Long squadraId) {
        return giocatoreRepo.findBySquadra_Id(squadraId).stream()
                .map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public GiocatoreDto findById(Long id) {
        return toDto(get(id));
    }

    // ── Statistiche ───────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public StatisticheDto getStatistiche(Long giocatoreId) {
        Statistiche s = statisticheRepo.findByGiocatore_Id(giocatoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistiche per giocatore", giocatoreId));
        return toStatDto(s);
    }

    public StatisticheDto aggiornaStatistiche(Long giocatoreId, AggiornaStatisticheRequest req) {
        Statistiche s = statisticheRepo.findByGiocatore_Id(giocatoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Statistiche per giocatore", giocatoreId));

        if (req.getPresenze()         != null) s.setPresenze(req.getPresenze());
        if (req.getGol()              != null) s.setGol(req.getGol());
        if (req.getAssist()           != null) s.setAssist(req.getAssist());
        if (req.getTiri()             != null) s.setTiri(req.getTiri());
        if (req.getTiriPorta()        != null) s.setTiriPorta(req.getTiriPorta());
        if (req.getPassaggi()         != null) s.setPassaggi(req.getPassaggi());
        if (req.getPassaggiRiusciti() != null) s.setPassaggiRiusciti(req.getPassaggiRiusciti());
        if (req.getDuelli()           != null) s.setDuelli(req.getDuelli());
        if (req.getDuelliVinti()      != null) s.setDuelliVinti(req.getDuelliVinti());
        if (req.getAmmonizioni()      != null) s.setAmmonizioni(req.getAmmonizioni());
        if (req.getEspulsioni()       != null) s.setEspulsioni(req.getEspulsioni());
        if (req.getMinutiGiocati()    != null) s.setMinutiGiocati(req.getMinutiGiocati());

        return toStatDto(statisticheRepo.save(s));
    }

    // ── Top marcatori ─────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<GiocatoreDto> topMarcatori(Long squadraId) {
        return giocatoreRepo.topMarcatori(squadraId).stream().map(this::toDto).toList();
    }

    // ── Helper ────────────────────────────────────────────────────────────
    Giocatore get(Long id) {
        return giocatoreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Giocatore", id));
    }

    Giocatore getByUtenteId(Long utenteId) {
        return giocatoreRepo.findByUtente_Id(utenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Giocatore per utente", utenteId));
    }

    public GiocatoreDto toDto(Giocatore g) {
        return GiocatoreDto.builder()
                .id(g.getId())
                .utenteId(g.getUtente().getId())
                .nome(g.getUtente().getNome())
                .cognome(g.getUtente().getCognome())
                .numeroMaglia(g.getNumeroMaglia())
                .ruoloCampo(g.getRuoloCampo())
                .dataNascita(g.getDataNascita())
                .nazionalita(g.getNazionalita())
                .fotoUrl(g.getFotoUrl())
                .puntiTotali(g.getPuntiTotali())
                .puntiSett(g.getPuntiSett())
                .build();
    }

    private StatisticheDto toStatDto(Statistiche s) {
        return StatisticheDto.builder()
                .giocatoreId(s.getGiocatore().getId())
                .presenze(s.getPresenze())
                .gol(s.getGol())
                .assist(s.getAssist())
                .tiri(s.getTiri())
                .tiriPorta(s.getTiriPorta())
                .passaggi(s.getPassaggi())
                .passaggiRiusciti(s.getPassaggiRiusciti())
                .duelli(s.getDuelli())
                .duelliVinti(s.getDuelliVinti())
                .ammonizioni(s.getAmmonizioni())
                .espulsioni(s.getEspulsioni())
                .minutiGiocati(s.getMinutiGiocati())
                .build();
    }
}
