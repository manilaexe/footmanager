package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ruolo")
@Getter @Setter @NoArgsConstructor
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RuoloNome nome;
}
