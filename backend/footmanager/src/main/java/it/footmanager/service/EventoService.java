package it.footmanager.service;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.*;
import it.footmanager.exception.*;
import it.footmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final EventoRepository  eventoRepo;
    private final SquadraRepository squadraRepo;
    private final UtenteRepository  utenteRepo;

    // ── Lista eventi squadra ──────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<EventoDto> findBySquadra(Long squadraId) {
        return eventoRepo.findBySquadra_IdOrderByDataInizioAsc(squadraId)
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<EventoDto> findByMese(Long squadraId, int anno, int mese) {
        YearMonth ym = YearMonth.of(anno, mese);
        return eventoRepo.findBySquadraAndMese(squadraId, ym.atDay(1), ym.atEndOfMonth())
                .stream().map(this::toDto).toList();
    }

    // ── Crea evento ───────────────────────────────────────────────────────
    public EventoDto crea(CreaEventoRequest req, String usernameCreatore) {
        Squadra squadra = squadraRepo.findById(req.getSquadraId())
                .orElseThrow(() -> new ResourceNotFoundException("Squadra", req.getSquadraId()));
        Utente creatore = utenteRepo.findByUsername(usernameCreatore)
                .orElseThrow(() -> new ResourceNotFoundException("Utente: " + usernameCreatore));

        Evento e = new Evento();
        e.setSquadra(squadra);
        e.setTitolo(req.getTitolo());
        e.setTipo(req.getTipo());
        e.setDataInizio(req.getDataInizio());
        e.setDataFine(req.getDataFine());
        e.setLuogo(req.getLuogo());
        e.setDescrizione(req.getDescrizione());
        e.setCreatoDA(creatore);

        return toDto(eventoRepo.save(e));
    }

    // ── Modifica evento ───────────────────────────────────────────────────
    public EventoDto aggiorna(Long id, CreaEventoRequest req) {
        Evento e = get(id);
        e.setTitolo(req.getTitolo());
        e.setTipo(req.getTipo());
        e.setDataInizio(req.getDataInizio());
        e.setDataFine(req.getDataFine());
        e.setLuogo(req.getLuogo());
        e.setDescrizione(req.getDescrizione());
        return toDto(eventoRepo.save(e));
    }

    // ── Elimina evento ────────────────────────────────────────────────────
    public void elimina(Long id) {
        eventoRepo.delete(get(id));
    }

    // ── Helper ────────────────────────────────────────────────────────────
    private Evento get(Long id) {
        return eventoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", id));
    }

    private EventoDto toDto(Evento e) {
        return EventoDto.builder()
                .id(e.getId())
                .titolo(e.getTitolo())
                .tipo(e.getTipo())
                .dataInizio(e.getDataInizio())
                .dataFine(e.getDataFine())
                .luogo(e.getLuogo())
                .descrizione(e.getDescrizione())
                .creatoDA(e.getCreatoDA().getNome() + " " + e.getCreatoDA().getCognome())
                .build();
    }
}
