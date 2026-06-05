package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calendario")
@Getter
@Setter
@NoArgsConstructor
public class Calendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calendar")
    private Long id;

    @Column(name = "permessi", length = 100)
    private String permessi;
}
