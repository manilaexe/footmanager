package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messaggio")
@Getter @Setter @NoArgsConstructor
public class Messaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mittente_id", nullable = false)
    private Utente mittente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Utente destinatario;

    @Column(nullable = false, length = 200)
    private String oggetto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String testo;

    @Column(name = "inviato_il", nullable = false, updatable = false)
    private LocalDateTime inviatoIl = LocalDateTime.now();

    @Column(name = "letto_il")
    private LocalDateTime lettoIl;

    public boolean isLetto() {
        return lettoIl != null;
    }
}
