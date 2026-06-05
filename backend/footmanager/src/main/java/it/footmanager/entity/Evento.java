package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
@Getter @Setter @NoArgsConstructor
public class Evento {

    public enum Tipo { ALLENAMENTO, PARTITA, RIUNIONE, ALTRO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "squadra_id", nullable = false)
    private Squadra squadra;

    @Column(nullable = false, length = 150)
    private String titolo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Tipo tipo;

    @Column(name = "data_inizio", nullable = false)
    private LocalDateTime dataInizio;

    @Column(name = "data_fine")
    private LocalDateTime dataFine;

    @Column(length = 200)
    private String luogo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creato_da", nullable = false)
    private Utente creatoDA;

    @Column(name = "creato_il", nullable = false, updatable = false)
    private LocalDateTime creatoIl = LocalDateTime.now();
}
