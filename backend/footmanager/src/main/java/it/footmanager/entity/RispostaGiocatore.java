package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "risposta_giocatore",
       uniqueConstraints = @UniqueConstraint(columnNames = {"giocatore_id","quiz_id"}))
@Getter @Setter @NoArgsConstructor
public class RispostaGiocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "giocatore_id", nullable = false)
    private Giocatore giocatore;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "risposta_data", nullable = false, length = 1)
    private String rispostaData;

    @Column(nullable = false)
    private boolean corretta;

    @Column(name = "secondi_impiegati")
    private Integer secondiImpiegati;

    @Column(name = "risposto_il", nullable = false, updatable = false)
    private LocalDateTime rispostoIl = LocalDateTime.now();
}
