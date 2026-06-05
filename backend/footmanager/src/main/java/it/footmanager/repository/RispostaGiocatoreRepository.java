package it.footmanager.repository;

import it.footmanager.entity.RispostaGiocatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RispostaGiocatoreRepository extends JpaRepository<RispostaGiocatore, Long> {
    boolean existsByGiocatore_IdAndQuiz_Id(Long giocatoreId, Long quizId);
    long countByGiocatore_IdAndCorrettaTrue(Long giocatoreId);
    List<RispostaGiocatore> findByGiocatore_Id(Long giocatoreId);
}
