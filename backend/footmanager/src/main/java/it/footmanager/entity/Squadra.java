package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "squadra")
@Getter @Setter @NoArgsConstructor
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 10)
    private String stagione = "2024/25";

    @Column(length = 80)
    private String citta;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;
}
