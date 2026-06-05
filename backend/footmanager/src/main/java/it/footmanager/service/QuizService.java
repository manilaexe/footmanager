package it.footmanager.service;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.*;
import it.footmanager.exception.*;
import it.footmanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {

    private final QuizRepository              quizRepo;
    private final RispostaGiocatoreRepository rispostaRepo;
    private final GiocatoreRepository         giocatoreRepo;
    private final BadgeRepository             badgeRepo;
    private final GiocatoreBadgeRepository    gbRepo;

    @Value("${app.quiz.timeout-seconds}")
    private int timeoutSeconds;

    // ── Quiz del giorno ───────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public QuizDto quizDelGiorno(Long giocatoreId) {
        Quiz quiz = quizRepo.findByDataPubblicazione(LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("Nessun quiz disponibile oggi"));
        boolean giaRisposto = rispostaRepo.existsByGiocatore_IdAndQuiz_Id(giocatoreId, quiz.getId());
        return toDto(quiz, giaRisposto);
    }

    // ── Risposta al quiz ──────────────────────────────────────────────────
    public RispostaQuizResponse rispondi(RispostaQuizRequest req, Long giocatoreId) {
        // Timeout lato server
        if (req.getSecondiImpiegati() > timeoutSeconds)
            throw new BadRequestException("Tempo scaduto (" + timeoutSeconds + "s)");

        Quiz quiz = quizRepo.findById(req.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", req.getQuizId()));

        if (rispostaRepo.existsByGiocatore_IdAndQuiz_Id(giocatoreId, quiz.getId()))
            throw new BadRequestException("Hai già risposto al quiz di oggi");

        Giocatore giocatore = giocatoreRepo.findById(giocatoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Giocatore", giocatoreId));

        boolean corretta = quiz.getRispostaCorretta().equalsIgnoreCase(req.getRisposta());

        // Salva risposta
        RispostaGiocatore rg = new RispostaGiocatore();
        rg.setGiocatore(giocatore);
        rg.setQuiz(quiz);
        rg.setRispostaData(req.getRisposta().toUpperCase());
        rg.setCorretta(corretta);
        rg.setSecondiImpiegati(req.getSecondiImpiegati());
        rispostaRepo.save(rg);

        // Aggiorna punti se risposta corretta
        int puntiAssegnati = 0;
        if (corretta) {
            puntiAssegnati = quiz.getPunti();
            giocatore.setPuntiTotali(giocatore.getPuntiTotali() + puntiAssegnati);
            giocatore.setPuntiSett(giocatore.getPuntiSett() + puntiAssegnati);
            giocatoreRepo.save(giocatore);
        }

        // Controlla badge sbloccabili
        List<BadgeDto> nuoviBadge = verificaBadge(giocatore);

        return RispostaQuizResponse.builder()
                .corretta(corretta)
                .rispostaCorretta(quiz.getRispostaCorretta())
                .puntiAssegnati(puntiAssegnati)
                .puntiTotali(giocatore.getPuntiTotali())
                .nuoviBadge(nuoviBadge)
                .build();
    }

    // ── Classifica settimanale ────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ClassificaItemDto> classificaSettimanale(Long squadraId) {
        List<Giocatore> lista = giocatoreRepo.classificaSettimanale(squadraId);
        List<ClassificaItemDto> result = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Giocatore g = lista.get(i);
            result.add(ClassificaItemDto.builder()
                    .posizione(i + 1)
                    .giocatoreId(g.getId())
                    .nome(g.getUtente().getNome())
                    .cognome(g.getUtente().getCognome())
                    .puntiSett(g.getPuntiSett())
                    .puntiTotali(g.getPuntiTotali())
                    .build());
        }
        return result;
    }

    // ── CRUD quiz (solo staff/allenatore/IT) ──────────────────────────────
    public QuizDto creaQuiz(Quiz quiz) {
        if (quizRepo.findByDataPubblicazione(quiz.getDataPubblicazione()).isPresent())
            throw new BadRequestException("Esiste già un quiz per " + quiz.getDataPubblicazione());
        return toDto(quizRepo.save(quiz), false);
    }

    // ── Badge check ───────────────────────────────────────────────────────
    private List<BadgeDto> verificaBadge(Giocatore giocatore) {
        long totaleCorrette = rispostaRepo.countByGiocatore_IdAndCorrettaTrue(giocatore.getId());
        List<Badge> candidati = badgeRepo.findBySogliaLessThanEqualOrderBySogliaAsc((int) totaleCorrette);
        List<BadgeDto> nuovi = new ArrayList<>();
        for (Badge b : candidati) {
            if (!gbRepo.existsByGiocatore_IdAndBadge_Id(giocatore.getId(), b.getId())) {
                GiocatoreBadge gb = new GiocatoreBadge();
                gb.setGiocatore(giocatore);
                gb.setBadge(b);
                gbRepo.save(gb);
                nuovi.add(toBadgeDto(b));
            }
        }
        return nuovi;
    }

    // ── Mapper ────────────────────────────────────────────────────────────
    private QuizDto toDto(Quiz q, boolean giaRisposto) {
        return QuizDto.builder()
                .id(q.getId())
                .domanda(q.getDomanda())
                .opzioneA(q.getOpzioneA())
                .opzioneB(q.getOpzioneB())
                .opzioneC(q.getOpzioneC())
                .opzioneD(q.getOpzioneD())
                .punti(q.getPunti())
                .dataPubblicazione(q.getDataPubblicazione())
                .giaRisposto(giaRisposto)
                .build();
    }

    private BadgeDto toBadgeDto(Badge b) {
        return BadgeDto.builder()
                .id(b.getId()).nome(b.getNome())
                .descrizione(b.getDescrizione())
                .icona(b.getIcona()).soglia(b.getSoglia())
                .build();
    }
}
