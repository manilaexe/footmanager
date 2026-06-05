package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "badge")
@Getter
@Setter
@NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_badge")
    private Long id;

    @Column(name = "nome_badge", nullable = false, length = 100)
    private String nome;

    @Lob
    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "soglia_punti", nullable = false)
    private Integer soglia;

    @Column(name = "immagine_icona", length = 255)
    private String icona;
}
