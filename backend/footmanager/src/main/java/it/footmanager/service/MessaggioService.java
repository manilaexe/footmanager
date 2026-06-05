package it.footmanager.service;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.*;
import it.footmanager.exception.*;
import it.footmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessaggioService {

    private final MessaggioRepository mesRepo;
    private final UtenteRepository    utenteRepo;

    @Transactional(readOnly = true)
    public List<MessaggioDto> inArrivo(Long destinatarioId) {
        return mesRepo.findByDestinatario_IdOrderByInviatoIlDesc(destinatarioId)
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<MessaggioDto> inviati(Long mittenteId) {
        return mesRepo.findByMittente_IdOrderByInviatoIlDesc(mittenteId)
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public long nonLetti(Long destinatarioId) {
        return mesRepo.countByDestinatario_IdAndLettoIlIsNull(destinatarioId);
    }

    public MessaggioDto invia(InviaMessaggioRequest req, String usernameMittente) {
        Utente mittente = utenteRepo.findByUsername(usernameMittente)
                .orElseThrow(() -> new ResourceNotFoundException("Utente: " + usernameMittente));
        Utente destinatario = utenteRepo.findById(req.getDestinatarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Utente", req.getDestinatarioId()));

        Messaggio m = new Messaggio();
        m.setMittente(mittente);
        m.setDestinatario(destinatario);
        m.setOggetto(req.getOggetto());
        m.setTesto(req.getTesto());
        return toDto(mesRepo.save(m));
    }

    /** Marca il messaggio come letto; verifica che il chiamante sia il destinatario */
    public MessaggioDto segnaLetto(Long messaggioId, String usernameDestinatario) {
        Messaggio m = mesRepo.findById(messaggioId)
                .orElseThrow(() -> new ResourceNotFoundException("Messaggio", messaggioId));
        if (!m.getDestinatario().getUsername().equals(usernameDestinatario))
            throw new BadRequestException("Non autorizzato a leggere questo messaggio");
        if (m.getLettoIl() == null) {
            m.setLettoIl(LocalDateTime.now());
            mesRepo.save(m);
        }
        return toDto(m);
    }

    private MessaggioDto toDto(Messaggio m) {
        return MessaggioDto.builder()
                .id(m.getId())
                .mittente(m.getMittente().getNome() + " " + m.getMittente().getCognome())
                .destinatario(m.getDestinatario().getNome() + " " + m.getDestinatario().getCognome())
                .oggetto(m.getOggetto())
                .testo(m.getTesto())
                .inviatoIl(m.getInviatoIl())
                .lettoIl(m.getLettoIl())
                .letto(m.isLetto())
                .build();
    }
}
