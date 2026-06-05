-- ============================================================
-- FootManager  –  V1  –  Schema iniziale
-- MySQL 8.0
-- ============================================================

-- ── RUOLI ────────────────────────────────────────────────────
CREATE TABLE ruolo (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    nome VARCHAR(30)  NOT NULL UNIQUE,     -- STAFF | ALLENATORE | GIOCATORE | DIRIGENZA | IT
    PRIMARY KEY (id)
);

-- ── UTENTI ───────────────────────────────────────────────────
CREATE TABLE utente (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    username       VARCHAR(50)   NOT NULL UNIQUE,
    password_hash  VARCHAR(100)  NOT NULL,
    nome           VARCHAR(60)   NOT NULL,
    cognome        VARCHAR(60)   NOT NULL,
    email          VARCHAR(120)  NOT NULL UNIQUE,
    attivo         BOOLEAN       NOT NULL DEFAULT TRUE,
    creato_il      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ruolo_id       BIGINT        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_utente_ruolo FOREIGN KEY (ruolo_id) REFERENCES ruolo(id)
);

-- ── SQUADRE ──────────────────────────────────────────────────
CREATE TABLE squadra (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    nome          VARCHAR(100) NOT NULL,
    stagione      VARCHAR(10)  NOT NULL DEFAULT '2024/25',
    citta         VARCHAR(80),
    logo_url      VARCHAR(255),
    PRIMARY KEY (id)
);

-- ── GIOCATORI ────────────────────────────────────────────────
CREATE TABLE giocatore (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    utente_id       BIGINT       NOT NULL UNIQUE,
    squadra_id      BIGINT       NOT NULL,
    numero_maglia   INT,
    ruolo_campo     VARCHAR(30),           -- Portiere, Difensore, Centrocampista, Attaccante
    data_nascita    DATE,
    nazionalita     VARCHAR(50),
    foto_url        VARCHAR(255),
    punti_totali    INT          NOT NULL DEFAULT 0,
    punti_sett      INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_giocatore_utente  FOREIGN KEY (utente_id)  REFERENCES utente(id),
    CONSTRAINT fk_giocatore_squadra FOREIGN KEY (squadra_id) REFERENCES squadra(id)
);

-- ── ALLENATORI ───────────────────────────────────────────────
CREATE TABLE allenatore (
    id          BIGINT  NOT NULL AUTO_INCREMENT,
    utente_id   BIGINT  NOT NULL UNIQUE,
    squadra_id  BIGINT  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_allenatore_utente  FOREIGN KEY (utente_id)  REFERENCES utente(id),
    CONSTRAINT fk_allenatore_squadra FOREIGN KEY (squadra_id) REFERENCES squadra(id)
);

-- ── STATISTICHE ──────────────────────────────────────────────
CREATE TABLE statistiche (
    id               BIGINT  NOT NULL AUTO_INCREMENT,
    giocatore_id     BIGINT  NOT NULL UNIQUE,
    presenze         INT     NOT NULL DEFAULT 0,
    gol              INT     NOT NULL DEFAULT 0,
    assist           INT     NOT NULL DEFAULT 0,
    tiri             INT     NOT NULL DEFAULT 0,
    tiri_porta       INT     NOT NULL DEFAULT 0,
    passaggi         INT     NOT NULL DEFAULT 0,
    passaggi_riusciti INT    NOT NULL DEFAULT 0,
    duelli           INT     NOT NULL DEFAULT 0,
    duelli_vinti     INT     NOT NULL DEFAULT 0,
    ammonizioni      INT     NOT NULL DEFAULT 0,
    espulsioni       INT     NOT NULL DEFAULT 0,
    minuti_giocati   INT     NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_stat_giocatore FOREIGN KEY (giocatore_id) REFERENCES giocatore(id)
);

-- ── EVENTI CALENDARIO ────────────────────────────────────────
CREATE TABLE evento (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    squadra_id     BIGINT        NOT NULL,
    titolo         VARCHAR(150)  NOT NULL,
    tipo           VARCHAR(30)   NOT NULL,   -- ALLENAMENTO | PARTITA | RIUNIONE | ALTRO
    data_inizio    DATETIME      NOT NULL,
    data_fine      DATETIME,
    luogo          VARCHAR(200),
    descrizione    TEXT,
    creato_da      BIGINT        NOT NULL,   -- FK utente
    creato_il      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_evento_squadra FOREIGN KEY (squadra_id)  REFERENCES squadra(id),
    CONSTRAINT fk_evento_utente  FOREIGN KEY (creato_da)   REFERENCES utente(id)
);

-- ── PRESENZE EVENTO ──────────────────────────────────────────
CREATE TABLE presenza_evento (
    evento_id     BIGINT      NOT NULL,
    giocatore_id  BIGINT      NOT NULL,
    stato         VARCHAR(20) NOT NULL DEFAULT 'PRESENTE',  -- PRESENTE | ASSENTE | GIUSTIFICATO
    PRIMARY KEY (evento_id, giocatore_id),
    CONSTRAINT fk_pres_evento    FOREIGN KEY (evento_id)    REFERENCES evento(id)    ON DELETE CASCADE,
    CONSTRAINT fk_pres_giocatore FOREIGN KEY (giocatore_id) REFERENCES giocatore(id) ON DELETE CASCADE
);

-- ── MESSAGGI ─────────────────────────────────────────────────
CREATE TABLE messaggio (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    mittente_id   BIGINT       NOT NULL,
    destinatario_id BIGINT     NOT NULL,
    oggetto       VARCHAR(200) NOT NULL,
    testo         TEXT         NOT NULL,
    inviato_il    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    letto_il      DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_msg_mittente      FOREIGN KEY (mittente_id)     REFERENCES utente(id),
    CONSTRAINT fk_msg_destinatario  FOREIGN KEY (destinatario_id) REFERENCES utente(id)
);

-- ── QUIZ ─────────────────────────────────────────────────────
CREATE TABLE quiz (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    domanda           TEXT         NOT NULL,
    opzione_a         VARCHAR(255) NOT NULL,
    opzione_b         VARCHAR(255) NOT NULL,
    opzione_c         VARCHAR(255) NOT NULL,
    opzione_d         VARCHAR(255) NOT NULL,
    risposta_corretta CHAR(1)      NOT NULL,  -- A | B | C | D
    punti             INT          NOT NULL DEFAULT 10,
    data_pubblicazione DATE        NOT NULL,  -- un quiz al giorno
    PRIMARY KEY (id)
);

-- ── RISPOSTE GIOCATORE ───────────────────────────────────────
CREATE TABLE risposta_giocatore (
    id               BIGINT    NOT NULL AUTO_INCREMENT,
    giocatore_id     BIGINT    NOT NULL,
    quiz_id          BIGINT    NOT NULL,
    risposta_data    CHAR(1)   NOT NULL,
    corretta         BOOLEAN   NOT NULL,
    secondi_impiegati INT,
    risposto_il      DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_giocatore_quiz (giocatore_id, quiz_id),  -- un solo tentativo/giorno
    CONSTRAINT fk_risp_giocatore FOREIGN KEY (giocatore_id) REFERENCES giocatore(id),
    CONSTRAINT fk_risp_quiz      FOREIGN KEY (quiz_id)      REFERENCES quiz(id)
);

-- ── BADGE ────────────────────────────────────────────────────
CREATE TABLE badge (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(80)  NOT NULL UNIQUE,
    descrizione VARCHAR(255),
    icona       VARCHAR(10),
    soglia      INT          NOT NULL,   -- numero risposte corrette necessarie
    PRIMARY KEY (id)
);

-- ── BADGE ASSEGNATI ──────────────────────────────────────────
CREATE TABLE giocatore_badge (
    giocatore_id  BIGINT   NOT NULL,
    badge_id      BIGINT   NOT NULL,
    ottenuto_il   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (giocatore_id, badge_id),
    CONSTRAINT fk_gb_giocatore FOREIGN KEY (giocatore_id) REFERENCES giocatore(id) ON DELETE CASCADE,
    CONSTRAINT fk_gb_badge     FOREIGN KEY (badge_id)     REFERENCES badge(id)
);

-- ============================================================
-- DATI INIZIALI
-- ============================================================

-- Ruoli
INSERT INTO ruolo (nome) VALUES
    ('STAFF'), ('ALLENATORE'), ('GIOCATORE'), ('DIRIGENZA'), ('IT');

-- Badge
INSERT INTO badge (nome, descrizione, icona, soglia) VALUES
    ('Principiante',      'Prime risposte corrette al quiz',       '🌱',  1),
    ('Apprendista storico','10 risposte corrette al quiz',         '📚', 10),
    ('Cultore del club',  '30 risposte corrette al quiz',         '🏅', 30),
    ('Esperto del club',  '60 risposte corrette al quiz',         '🎖', 60),
    ('Bandiera del club', 'Oltre 90 risposte corrette al quiz',   '⭐', 90);

-- Squadra demo
INSERT INTO squadra (nome, stagione, citta) VALUES
    ('Bologna FC Demo', '2024/25', 'Bologna');

-- Utente staff demo (password: staff123 → bcrypt)
INSERT INTO utente (username, password_hash, nome, cognome, email, ruolo_id)
VALUES ('staff1',
        '$2a$12$6Q.7TiHPG7v.mCgpW1eISuz3c.gQg8lT97tR5q1JrMYgzlxmhWnl2',
        'Mario', 'Staff', 'staff@footmanager.it',
        (SELECT id FROM ruolo WHERE nome='STAFF'));

-- Utente allenatore demo (password: coach123)
INSERT INTO utente (username, password_hash, nome, cognome, email, ruolo_id)
VALUES ('allenatore',
        '$2a$12$pS5UmnnE8iGxkfIBxQ3WUO6LiNGT5mZy/hB7s2DCiAaSqJgCuKRuO',
        'Luca', 'Allenatore', 'coach@footmanager.it',
        (SELECT id FROM ruolo WHERE nome='ALLENATORE'));

INSERT INTO allenatore (utente_id, squadra_id)
VALUES (
    (SELECT id FROM utente WHERE username='allenatore'),
    (SELECT id FROM squadra WHERE nome='Bologna FC Demo')
);

-- Utente giocatore demo (password: play123)
INSERT INTO utente (username, password_hash, nome, cognome, email, ruolo_id)
VALUES ('giocatore1',
        '$2a$12$FyMlhHl5ZiVhVKlUvCbg0OqBB4IfBXp7eTUCWjkFgfXSLJ4z1Sxb.',
        'Lorenzo', 'Rossi', 'lrossi@footmanager.it',
        (SELECT id FROM ruolo WHERE nome='GIOCATORE'));

INSERT INTO giocatore (utente_id, squadra_id, numero_maglia, ruolo_campo)
VALUES (
    (SELECT id FROM utente WHERE username='giocatore1'),
    (SELECT id FROM squadra WHERE nome='Bologna FC Demo'),
    10, 'Attaccante'
);

INSERT INTO statistiche (giocatore_id)
VALUES ((SELECT id FROM giocatore WHERE numero_maglia=10));

-- Utente dirigente demo (password: dir123)
INSERT INTO utente (username, password_hash, nome, cognome, email, ruolo_id)
VALUES ('dirigente1',
        '$2a$12$ER2K6FgMKXpqJHtolObhfeXOcFiWXHPXqNV5wq8nSbBGNQFWm4rSu',
        'Carlo', 'Ferrari', 'dirigenza@footmanager.it',
        (SELECT id FROM ruolo WHERE nome='DIRIGENZA'));
