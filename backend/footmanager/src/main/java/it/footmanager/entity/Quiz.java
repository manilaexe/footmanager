package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz")
@Getter
@Setter
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quiz")
    private Long id;

    @Lob
    @Column(name = "domanda", nullable = false)
    private String domanda;

    @Column(name = "risposta_corretta", nullable = false, length = 255)
    private String rispostaCorretta;

    @Column(name = "opzione_2", nullable = false, length = 255)
    private String opzione2;

    @Column(name = "opzione_3", nullable = false, length = 255)
    private String opzione3;

    @Column(name = "punti_valore", nullable = false)
    private Integer punti = 0;
}
