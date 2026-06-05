package it.footmanager.repository;

import it.footmanager.entity.Ruolo;
import it.footmanager.entity.RuoloNome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Optional<Ruolo> findByNome(RuoloNome nome);
}
