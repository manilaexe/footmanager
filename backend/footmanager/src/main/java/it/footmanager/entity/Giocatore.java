package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "giocatore")
@Getter @Setter @NoArgsConstructor
public class Giocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id", nullable = false, unique = true)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "squadra_id", nullable = false)
    private Squadra squadra;

    @Column(name = "numero_maglia")
    private Integer numeroMaglia;

    @Column(name = "ruolo_campo", length = 30)
    private String ruoloCampo;

    @Column(name = "data_nascita")
    private LocalDate dataNascita;

    @Column(length = 50)
    private String nazionalita;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "punti_totali", nullable = false)
    private int puntiTotali = 0;

    @Column(name = "punti_sett", nullable = false)
    private int puntiSett = 0;

    /** Statistiche (relazione 1:1 lazy) */
    @OneToOne(mappedBy = "giocatore", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Statistiche statistiche;

    /** Badge conquistati */
    @OneToMany(mappedBy = "giocatore", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiocatoreBadge> badges = new ArrayList<>();
}
