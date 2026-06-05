package it.footmanager.dto;

import it.footmanager.entity.Evento;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ─────────────────────────────────────────────────────────────────────────
// UTENTE
// ─────────────────────────────────────────────────────────────────────────
public class Dtos {

    @Data @Builder
    public static class UtenteDto {
        private Long   id;
        private String username;
        private String nome;
        private String cognome;
        private String email;
        private String ruolo;
        private boolean attivo;
    }

    @Data
    public static class CreaUtenteRequest {
        @NotBlank @Size(min = 3, max = 50) private String username;
        @NotBlank @Size(min = 6)            private String password;
        @NotBlank                           private String nome;
        @NotBlank                           private String cognome;
        @Email @NotBlank                    private String email;
        @NotBlank                           private String ruolo;     // "ALLENATORE" | "GIOCATORE" | …
        private Long squadraId;     // obbligatorio per GIOCATORE/ALLENATORE
        private Integer numeroMaglia;
        private String  ruoloCampo;
    }

    // ─────────────────────────────────────────────────────────────────────
    // GIOCATORE
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class GiocatoreDto {
        private Long    id;
        private Long    utenteId;
        private String  nome;
        private String  cognome;
        private Integer numeroMaglia;
        private String  ruoloCampo;
        private LocalDate dataNascita;
        private String  nazionalita;
        private String  fotoUrl;
        private int     puntiTotali;
        private int     puntiSett;
    }

    // ─────────────────────────────────────────────────────────────────────
    // STATISTICHE
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class StatisticheDto {
        private Long   giocatoreId;
        private int    presenze;
        private int    gol;
        private int    assist;
        private int    tiri;
        private int    tiriPorta;
        private int    passaggi;
        private int    passaggiRiusciti;
        private int    duelli;
        private int    duelliVinti;
        private int    ammonizioni;
        private int    espulsioni;
        private int    minutiGiocati;
    }

    @Data
    public static class AggiornaStatisticheRequest {
        private Integer presenze;
        private Integer gol;
        private Integer assist;
        private Integer tiri;
        private Integer tiriPorta;
        private Integer passaggi;
        private Integer passaggiRiusciti;
        private Integer duelli;
        private Integer duelliVinti;
        private Integer ammonizioni;
        private Integer espulsioni;
        private Integer minutiGiocati;
    }

    // ─────────────────────────────────────────────────────────────────────
    // EVENTO
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class EventoDto {
        private Long          id;
        private String        titolo;
        private Evento.Tipo   tipo;
        private LocalDateTime dataInizio;
        private LocalDateTime dataFine;
        private String        luogo;
        private String        descrizione;
        private String        creatoDA;
    }

    @Data
    public static class CreaEventoRequest {
        @NotBlank                           private String titolo;
        @NotNull                            private Evento.Tipo tipo;
        @NotNull                            private LocalDateTime dataInizio;
        private LocalDateTime dataFine;
        private String        luogo;
        private String        descrizione;
        @NotNull                            private Long squadraId;
    }

    // ─────────────────────────────────────────────────────────────────────
    // MESSAGGIO
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class MessaggioDto {
        private Long          id;
        private String        mittente;
        private String        destinatario;
        private String        oggetto;
        private String        testo;
        private LocalDateTime inviatoIl;
        private LocalDateTime lettoIl;
        private boolean       letto;
    }

    @Data
    public static class InviaMessaggioRequest {
        @NotNull                private Long   destinatarioId;
        @NotBlank               private String oggetto;
        @NotBlank               private String testo;
    }

    // ─────────────────────────────────────────────────────────────────────
    // QUIZ
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class QuizDto {
        private Long      id;
        private String    domanda;
        private String    opzioneA;
        private String    opzioneB;
        private String    opzioneC;
        private String    opzioneD;
        private int       punti;
        private LocalDate dataPubblicazione;
        private boolean   giaRisposto;
    }

    @Data
    public static class RispostaQuizRequest {
        @NotNull  private Long   quizId;
        @NotBlank private String risposta;      // "A" | "B" | "C" | "D"
        @Min(0)   private int    secondiImpiegati;
    }

    @Data @Builder
    public static class RispostaQuizResponse {
        private boolean corretta;
        private String  rispostaCorretta;
        private int     puntiAssegnati;
        private int     puntiTotali;
        private List<BadgeDto> nuoviBadge;
    }

    // ─────────────────────────────────────────────────────────────────────
    // BADGE
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class BadgeDto {
        private Long   id;
        private String nome;
        private String descrizione;
        private String icona;
        private int    soglia;
    }

    // ─────────────────────────────────────────────────────────────────────
    // CLASSIFICA
    // ─────────────────────────────────────────────────────────────────────
    @Data @Builder
    public static class ClassificaItemDto {
        private int    posizione;
        private Long   giocatoreId;
        private String nome;
        private String cognome;
        private int    puntiSett;
        private int    puntiTotali;
    }
}
