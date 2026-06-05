package it.footmanager.scheduler;

import it.footmanager.entity.GiocatoreBadge;
import it.footmanager.entity.Giocatore;
import it.footmanager.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Job settimanale schedulato ogni lunedì alle 00:05.
 * 1. Resetta puntiSett di tutti i giocatori a 0
 * 2. Assegna eventuali badge non ancora conquistati
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuizScheduler {

    private final GiocatoreRepository         giocatoreRepo;
    private final BadgeRepository             badgeRepo;
    private final GiocatoreBadgeRepository    gbRepo;
    private final RispostaGiocatoreRepository rispostaRepo;

    @Scheduled(cron = "0 5 0 * * MON", zone = "Europe/Rome")
    @Transactional
    public void resetClassificaSettimanale() {
        log.info("Avvio reset classifica settimanale quiz...");
        List<Giocatore> giocatori = giocatoreRepo.findAll();
        for (Giocatore g : giocatori) {
            assegnaBadgeSeNecessario(g);
            g.setPuntiSett(0);
        }
        giocatoreRepo.saveAll(giocatori);
        log.info("Reset classifica completato per {} giocatori.", giocatori.size());
    }

    private void assegnaBadgeSeNecessario(Giocatore g) {
        long totCorrette = rispostaRepo.countByGiocatore_IdAndCorrettaTrue(g.getId());
        badgeRepo.findBySogliaLessThanEqualOrderBySogliaAsc((int) totCorrette).forEach(badge -> {
            if (!gbRepo.existsByGiocatore_IdAndBadge_Id(g.getId(), badge.getId())) {
                GiocatoreBadge gb = new GiocatoreBadge();
                gb.setGiocatore(g);
                gb.setBadge(badge);
                gbRepo.save(gb);
                log.info("Badge '{}' assegnato a {}", badge.getNome(), g.getUtente().getUsername());
            }
        });
    }
}
