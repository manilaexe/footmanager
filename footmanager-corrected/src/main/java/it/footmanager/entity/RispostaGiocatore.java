package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risposta_utente")
@Getter @Setter @NoArgsConstructor
public class RispostaGiocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_risposta")
    private Integer id;

    @Column(name = "data_risposta", nullable = false)
    private LocalDateTime dataRisposta;

    @Column(name = "tempo_impiegato_sec")
    private Integer tempoImpiegatoSec;

    // Nel DB: "esito" (boolean/tinyint), non "corretta"
    @Column(name = "esito", nullable = false)
    private boolean esito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_giocatore")
    private Giocatore giocatore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quiz")
    private Quiz quiz;

    @PrePersist
    protected void onCreate() {
        if (dataRisposta == null) dataRisposta = LocalDateTime.now();
    }
}
