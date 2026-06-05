package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ruolo")
@Getter
@Setter
@NoArgsConstructor
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruolo")
    private Long id;

    @Column(name = "nome_ruolo", nullable = false, length = 50)
    private String nome;
}
