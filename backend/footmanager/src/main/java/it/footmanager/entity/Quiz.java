package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "quiz")
@Getter @Setter @NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String domanda;

    @Column(name = "opzione_a", nullable = false, length = 255)
    private String opzioneA;

    @Column(name = "opzione_b", nullable = false, length = 255)
    private String opzioneB;

    @Column(name = "opzione_c", nullable = false, length = 255)
    private String opzioneC;

    @Column(name = "opzione_d", nullable = false, length = 255)
    private String opzioneD;

    @Column(name = "risposta_corretta", nullable = false, length = 1)
    private String rispostaCorretta;   // "A" | "B" | "C" | "D"

    @Column(nullable = false)
    private int punti = 10;

    @Column(name = "data_pubblicazione", nullable = false, unique = true)
    private LocalDate dataPubblicazione;
}
