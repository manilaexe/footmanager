package it.footmanager.service;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.*;
import it.footmanager.exception.*;
import it.footmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtenteService {

    private final UtenteRepository      utenteRepo;
    private final RuoloRepository       ruoloRepo;
    private final SquadraRepository     squadraRepo;
    private final GiocatoreRepository   giocatoreRepo;
    private final AllenatoreRepository  allenatoreRepo;
    private final StatisticheRepository statisticheRepo;
    private final PasswordEncoder       encoder;

    // ── Lista utenti ─────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<UtenteDto> findAll() {
        return utenteRepo.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public UtenteDto findById(Long id) {
        return toDto(getUtente(id));
    }

    // ── Crea utente + entità collegata ───────────────────────────────────
    public UtenteDto crea(CreaUtenteRequest req) {
        if (utenteRepo.existsByUsername(req.getUsername()))
            throw new BadRequestException("Username '" + req.getUsername() + "' già in uso");
        if (utenteRepo.existsByEmail(req.getEmail()))
            throw new BadRequestException("Email '" + req.getEmail() + "' già registrata");

        RuoloNome ruoloNome = parseRuolo(req.getRuolo());
        Ruolo     ruolo     = ruoloRepo.findByNome(ruoloNome)
                .orElseThrow(() -> new BadRequestException("Ruolo non valido: " + req.getRuolo()));

        Utente utente = new Utente();
        utente.setUsername(req.getUsername());
        utente.setPasswordHash(encoder.encode(req.getPassword()));
        utente.setNome(req.getNome());
        utente.setCognome(req.getCognome());
        utente.setEmail(req.getEmail());
        utente.setRuolo(ruolo);
        utenteRepo.save(utente);

        // Crea l'entità specializzata in base al ruolo
        switch (ruoloNome) {
            case GIOCATORE -> {
                Squadra sq = getSquadra(req.getSquadraId());
                Giocatore g = new Giocatore();
                g.setUtente(utente);
                g.setSquadra(sq);
                g.setNumeroMaglia(req.getNumeroMaglia());
                g.setRuoloCampo(req.getRuoloCampo());
                giocatoreRepo.save(g);
                // crea riga statistiche vuota
                Statistiche s = new Statistiche();
                s.setGiocatore(g);
                statisticheRepo.save(s);
            }
            case ALLENATORE -> {
                Squadra sq = getSquadra(req.getSquadraId());
                Allenatore a = new Allenatore();
                a.setUtente(utente);
                a.setSquadra(sq);
                allenatoreRepo.save(a);
            }
            default -> { /* STAFF, DIRIGENZA, IT: nessuna entità extra */ }
        }

        return toDto(utente);
    }

    // ── Aggiorna dati base ───────────────────────────────────────────────
    public UtenteDto aggiorna(Long id, CreaUtenteRequest req) {
        Utente utente = getUtente(id);
        if (req.getNome()    != null) utente.setNome(req.getNome());
        if (req.getCognome() != null) utente.setCognome(req.getCognome());
        if (req.getEmail()   != null) utente.setEmail(req.getEmail());
        if (req.getPassword()!= null && !req.getPassword().isBlank())
            utente.setPasswordHash(encoder.encode(req.getPassword()));
        return toDto(utenteRepo.save(utente));
    }

    // ── Disattiva (soft delete) ──────────────────────────────────────────
    public void disattiva(Long id) {
        Utente u = getUtente(id);
        u.setAttivo(false);
        utenteRepo.save(u);
    }

    // ── Helper ───────────────────────────────────────────────────────────
    private Utente getUtente(Long id) {
        return utenteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente", id));
    }

    private Squadra getSquadra(Long id) {
        if (id == null) throw new BadRequestException("squadraId obbligatorio per questo ruolo");
        return squadraRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Squadra", id));
    }

    private RuoloNome parseRuolo(String r) {
        try { return RuoloNome.valueOf(r.toUpperCase()); }
        catch (IllegalArgumentException e) { throw new BadRequestException("Ruolo non valido: " + r); }
    }

    public UtenteDto toDto(Utente u) {
        return UtenteDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .nome(u.getNome())
                .cognome(u.getCognome())
                .email(u.getEmail())
                .ruolo(u.getRuolo().getNome().name())
                .attivo(u.isAttivo())
                .build();
    }
}
