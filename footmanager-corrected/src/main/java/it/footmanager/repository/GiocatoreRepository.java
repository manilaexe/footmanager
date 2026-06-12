package it.footmanager.repository;

import it.footmanager.entity.Giocatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GiocatoreRepository extends JpaRepository<Giocatore, Integer> {
    Optional<Giocatore> findByUtente_Id(Integer utenteId);
    List<Giocatore>     findBySquadra_Id(Integer squadraId);

    @Query("""
        SELECT g FROM Giocatore g JOIN FETCH g.utente JOIN g.statistiche s
        WHERE g.squadra.id = :squadraId
        ORDER BY (s.goalRigore + s.goalDiTesta + s.goalPunizione) DESC
        """)
    List<Giocatore> topMarcatori(@Param("squadraId") Integer squadraId);
}
