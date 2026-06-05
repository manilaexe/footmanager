package it.footmanager.repository;

import it.footmanager.entity.Utente;
import it.footmanager.entity.RuoloNome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Utente> findByRuolo_Nome(RuoloNome nome);
}
