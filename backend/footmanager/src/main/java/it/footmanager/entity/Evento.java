package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;

    @Column(name = "titolo", nullable = false, length = 100)
    private String titolo;

    @Column(name = "data_ora_inizio", nullable = false)
    private LocalDateTime dataOraInizio;

    @Column(name = "data_ora_fine", nullable = false)
    private LocalDateTime dataOraFine;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "luogo", length = 100)
    private String luogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_calendar")
    private Calendario calendario;
}
