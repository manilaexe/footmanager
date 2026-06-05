package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "badge")
@Getter @Setter @NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(length = 255)
    private String descrizione;

    @Column(length = 10)
    private String icona;

    /** Numero risposte corrette necessarie per sbloccare */
    @Column(nullable = false)
    private int soglia;
}
