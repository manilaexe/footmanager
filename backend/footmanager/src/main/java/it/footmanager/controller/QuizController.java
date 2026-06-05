package it.footmanager.controller;

import it.footmanager.dto.Dtos.*;
import it.footmanager.entity.Quiz;
import it.footmanager.repository.GiocatoreRepository;
import it.footmanager.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService          quizService;
    private final GiocatoreRepository  giocatoreRepo;

    /** Quiz del giorno per il giocatore autenticato */
    @GetMapping("/oggi")
    @PreAuthorize("hasAnyRole('GIOCATORE','STAFF','IT')")
    public QuizDto quizOggi(@AuthenticationPrincipal UserDetails ud) {
        Long gid = giocatoreRepo.findByUtente_Id(
                giocatoreRepo.findAll().stream()   // ricava id giocatore dal username
                        .filter(g -> g.getUtente().getUsername().equals(ud.getUsername()))
                        .findFirst().orElseThrow().getId())
                .orElseThrow().getId();
        return quizService.quizDelGiorno(gid);
    }

    /** Invia risposta */
    @PostMapping("/risposta")
    @PreAuthorize("hasRole('GIOCATORE')")
    public RispostaQuizResponse rispondi(@Valid @RequestBody RispostaQuizRequest req,
                                         @AuthenticationPrincipal UserDetails ud) {
        Long gid = giocatoreRepo.findAll().stream()
                .filter(g -> g.getUtente().getUsername().equals(ud.getUsername()))
                .findFirst().orElseThrow().getId();
        return quizService.rispondi(req, gid);
    }

    /** Classifica settimanale squadra */
    @GetMapping("/classifica/{squadraId}")
    public List<ClassificaItemDto> classifica(@PathVariable Long squadraId) {
        return quizService.classificaSettimanale(squadraId);
    }

    /** Crea un nuovo quiz (solo STAFF/ALLENATORE/IT) */
    @PostMapping
    @PreAuthorize("hasAnyRole('STAFF','ALLENATORE','IT')")
    public ResponseEntity<QuizDto> creaQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(quizService.creaQuiz(quiz));
    }
}
