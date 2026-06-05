package it.footmanager.repository;

import it.footmanager.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findBySquadra_IdOrderByDataInizioAsc(Long squadraId);

    @Query("""
        SELECT e FROM Evento e
        WHERE e.squadra.id = :sid
          AND FUNCTION('DATE', e.dataInizio) BETWEEN :da AND :a
        ORDER BY e.dataInizio ASC
        """)
    List<Evento> findBySquadraAndMese(@Param("sid") Long squadraId,
                                      @Param("da")  LocalDate da,
                                      @Param("a")   LocalDate a);
}
