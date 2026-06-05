package it.footmanager.repository;

import it.footmanager.entity.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessaggioRepository extends JpaRepository<Messaggio, Long> {
    List<Messaggio> findByDestinatario_IdOrderByInviatoIlDesc(Long destinatarioId);
    List<Messaggio> findByMittente_IdOrderByInviatoIlDesc(Long mittenteId);
    long countByDestinatario_IdAndLettoIlIsNull(Long destinatarioId);
}
