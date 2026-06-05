package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statistiche")
@Getter @Setter @NoArgsConstructor
public class Statistiche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "giocatore_id", nullable = false, unique = true)
    private Giocatore giocatore;

    private int presenze          = 0;
    private int gol               = 0;
    private int assist            = 0;
    private int tiri              = 0;

    @Column(name = "tiri_porta")
    private int tiriPorta         = 0;

    private int passaggi          = 0;

    @Column(name = "passaggi_riusciti")
    private int passaggiRiusciti  = 0;

    private int duelli            = 0;

    @Column(name = "duelli_vinti")
    private int duelliVinti       = 0;

    private int ammonizioni       = 0;
    private int espulsioni        = 0;

    @Column(name = "minuti_giocati")
    private int minutiGiocati     = 0;
}
