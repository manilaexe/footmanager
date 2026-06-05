package it.footmanager.repository;

import it.footmanager.entity.Giocatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {
    Optional<Giocatore> findByUtente_Id(Long utenteId);
    List<Giocatore>     findBySquadra_Id(Long squadraId);

    @Query("SELECT g FROM Giocatore g JOIN FETCH g.utente WHERE g.squadra.id = :squadraId ORDER BY g.puntiSett DESC")
    List<Giocatore> classificaSettimanale(@Param("squadraId") Long squadraId);

    @Query("""
        SELECT g FROM Giocatore g
        JOIN FETCH g.utente
        JOIN g.statistiche s
        WHERE g.squadra.id = :squadraId
        ORDER BY s.gol DESC
        """)
    List<Giocatore> topMarcatori(@Param("squadraId") Long squadraId);
}
