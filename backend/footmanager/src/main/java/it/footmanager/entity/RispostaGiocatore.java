package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "risposta_utente")
@Getter
@Setter
@NoArgsConstructor
public class RispostaUtente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_risposta")
    private Long id;

    @Column(name = "data_risposta", nullable = false)
    private LocalDateTime dataRisposta;

    @Column(name = "tempo_impiegato_sec")
    private Integer tempoImpiegatoSec;

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
        if (dataRisposta == null) {
            dataRisposta = LocalDateTime.now();
        }
    }
}
