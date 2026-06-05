package it.footmanager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "giocatore_badge")
@Getter @Setter @NoArgsConstructor
@IdClass(GiocatoreBadge.GiocatoreBadgeId.class)
public class GiocatoreBadge {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giocatore_id")
    private Giocatore giocatore;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    @Column(name = "ottenuto_il", nullable = false, updatable = false)
    private LocalDateTime ottenutoIl = LocalDateTime.now();

    // ── Chiave composta ──────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GiocatoreBadgeId implements Serializable {
        private Long giocatore;
        private Long badge;
    }
}
