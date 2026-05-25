-- Creazione del database (opzionale, decommenta se serve)
-- CREATE DATABASE IF NOT EXISTS footmanager_db;
-- USE footmanager_db;

-- -----------------------------------------------------
-- 1. Tabella RUOLO
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS RUOLO (
    id_ruolo INT AUTO_INCREMENT PRIMARY KEY,
    nome_ruolo VARCHAR(50) NOT NULL
);

-- -----------------------------------------------------
-- 2. Tabella UTENTE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS UTENTE (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_ruolo INT,
    FOREIGN KEY (id_ruolo) REFERENCES RUOLO(id_ruolo)
		ON DELETE SET NULL ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 3. Tabella CALENDARIO
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CALENDARIO (
    id_calendar INT AUTO_INCREMENT PRIMARY KEY,
    permessi VARCHAR(100)
);

-- -----------------------------------------------------
-- 4. Tabella EVENTO
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS EVENTO (
    id_evento INT AUTO_INCREMENT PRIMARY KEY,
    titolo VARCHAR(100) NOT NULL,
    data_ora_inizio DATETIME NOT NULL,
    data_ora_fine DATETIME NOT NULL,
    tipo VARCHAR(50),
    luogo VARCHAR(100),
    id_calendar INT,
    FOREIGN KEY (id_calendar) REFERENCES CALENDARIO(id_calendar)
		ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 5. Tabella SQUADRA
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SQUADRA (
    id_squadra INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50)
);

-- -----------------------------------------------------
-- 6. Tabella GIOCATORE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS GIOCATORE (
    id_giocatore INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    numero INT,
    img VARCHAR(255),
    piede VARCHAR(10),
    posizione VARCHAR(50),
    nazionalità VARCHAR(50),
    altezza INT, -- espressa in cm
    id_squadra INT,
    id_user INT,
    FOREIGN KEY (id_squadra) REFERENCES SQUADRA(id_squadra)
		ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (id_user) REFERENCES UTENTE(id_user)
		ON DELETE SET NULL ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 7. Tabella ALLENATORE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ALLENATORE (
    id_allenatore INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    id_squadra INT,
    id_user INT,
    FOREIGN KEY (id_squadra) REFERENCES SQUADRA(id_squadra)
		ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (id_user) REFERENCES UTENTE(id_user)
		ON DELETE SET NULL ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 8. Tabella MESSAGGIO
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS MESSAGGIO (
    id_messaggio INT AUTO_INCREMENT PRIMARY KEY,
    testo TEXT NOT NULL,
    data_ora DATETIME NOT NULL,
    stato VARCHAR(20), -- es. 'inviato', 'letto'
    id_giocatore INT,
    id_allenatore INT,
    FOREIGN KEY (id_giocatore) REFERENCES GIOCATORE(id_giocatore)
		ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_allenatore) REFERENCES ALLENATORE(id_allenatore)
		ON DELETE SET NULL ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 9. Tabella STATISTICHE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS STATISTICHE (
    id_statistica INT AUTO_INCREMENT PRIMARY KEY,
    presenze INT DEFAULT 0,
    presenze_titolare INT DEFAULT 0,
    minuti_giocati INT DEFAULT 0,
    goal_rigore INT DEFAULT 0,
    goal_di_testa INT DEFAULT 0,
    goal_punizione INT DEFAULT 0,
    assist INT DEFAULT 0,
    tiri_totali INT DEFAULT 0,
    tiri_in_porta INT DEFAULT 0,
    pali_traverse INT DEFAULT 0,
    big_chance_mancate INT DEFAULT 0,
    big_chance_create INT DEFAULT 0,
    passaggi_tentati INT DEFAULT 0,
    passaggi_riusciti INT DEFAULT 0,
    passaggi_chiave INT DEFAULT 0,
    cross_tentati INT DEFAULT 0,
    cross_riusciti INT DEFAULT 0,
    dribbling_tentati INT DEFAULT 0,
    dribbling_riusciti INT DEFAULT 0,
    duelli_vinti INT DEFAULT 0,
    duelli_persi INT DEFAULT 0,
    duelli_aerei_vinti INT DEFAULT 0,
    duelli_aerei_persi INT DEFAULT 0,
    palloni_rubati INT DEFAULT 0,
    palloni_intercettati INT DEFAULT 0,
    tackle INT DEFAULT 0,
    falli_commessi INT DEFAULT 0,
    falli_subiti INT DEFAULT 0,
    ammonizioni INT DEFAULT 0,
    espulsioni INT DEFAULT 0,
    id_giocatore INT UNIQUE, -- Mantiene la relazione 1:1 logica
    FOREIGN KEY (id_giocatore) REFERENCES GIOCATORE(id_giocatore)
		ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 10. Tabella QUIZ
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS QUIZ (
    id_quiz INT AUTO_INCREMENT PRIMARY KEY,
    domanda TEXT NOT NULL,
    risposta_corretta VARCHAR(255) NOT NULL,
    opzione_2 VARCHAR(255) NOT NULL,
    opzione_3 VARCHAR(255) NOT NULL,
    punti_valore INT DEFAULT 0
);

-- -----------------------------------------------------
-- 11. Tabella RISPOSTA_UTENTE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS RISPOSTA_UTENTE (
    id_risposta INT AUTO_INCREMENT PRIMARY KEY,
    data_risposta DATETIME NOT NULL,
    tempo_impiegato_sec INT,
    esito BOOLEAN NOT NULL, -- TRUE = Corretta, FALSE = Errata
    id_giocatore INT,
    id_quiz INT,
    FOREIGN KEY (id_giocatore) REFERENCES GIOCATORE(id_giocatore)
		ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_quiz) REFERENCES QUIZ(id_quiz)
		ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- 12. Tabella BADGE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS BADGE (
    id_badge INT AUTO_INCREMENT PRIMARY KEY,
    nome_badge VARCHAR(100) NOT NULL,
    descrizione TEXT,
    soglia_punti INT DEFAULT 0,
    immagine_icona VARCHAR(255)
);

-- -----------------------------------------------------
-- 13. Tabella GIOCATORE_BADGE (Relazione Many-to-Many)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS GIOCATORE_BADGE (
    id_giocatore INT,
    id_badge INT,
    data_ottenimento DATETIME NOT NULL,
    PRIMARY KEY (id_giocatore, id_badge), -- Chiave primaria composta
    FOREIGN KEY (id_giocatore) REFERENCES GIOCATORE(id_giocatore)
		ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_badge) REFERENCES BADGE(id_badge)
		ON DELETE CASCADE ON UPDATE CASCADE
);
