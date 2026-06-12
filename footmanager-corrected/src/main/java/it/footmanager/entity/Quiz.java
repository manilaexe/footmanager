package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz")
@Getter @Setter @NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quiz")
    private Integer id;

    @Column(name = "domanda", nullable = false, columnDefinition = "TEXT")
    private String domanda;

    // Prima opzione — è quella corretta
    @Column(name = "risposta_corretta", nullable = false, length = 255)
    private String rispostaCorretta;

    @Column(name = "opzione_2", nullable = false, length = 255)
    private String opzione2;

    @Column(name = "opzione_3", nullable = false, length = 255)
    private String opzione3;

    @Column(name = "punti_valore")
    private int puntiValore = 0;
}
