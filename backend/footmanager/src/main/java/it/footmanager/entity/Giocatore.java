package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "giocatore")
@Getter
@Setter
@NoArgsConstructor
public class Giocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_giocatore")
    private Long id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 50)
    private String cognome;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "img", length = 255)
    private String img;

    @Column(name = "piede", length = 10)
    private String piede;

    @Column(name = "posizione", length = 50)
    private String posizione;

    @Column(name = "nazionalità", length = 50)
    private String nazionalita;

    @Column(name = "altezza")
    private Integer altezza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_squadra")
    private Squadra squadra;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Utente utente;
}
