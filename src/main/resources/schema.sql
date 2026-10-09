-- Création de la base de données
CREATE DATABASE IF NOT EXISTS dbVentes
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE dbVentes;

-- ─── Table users ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id_user           INT AUTO_INCREMENT PRIMARY KEY,
    login             VARCHAR(50)  NOT NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    connection_number INT          DEFAULT 0
);

-- ─── Table articles ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS articles (
    id_article  INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    brand       VARCHAR(100) NOT NULL,
    price       DOUBLE       NOT NULL,
    stock       INT          NOT NULL DEFAULT 0
);

-- ─── Table commands ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS commands (
    id_command    INT AUTO_INCREMENT PRIMARY KEY,
    command_date  DATE   NOT NULL,
    total_amount  DOUBLE DEFAULT 0.0,
    user_id       INT    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id_user)
);

-- ─── Table command_lines ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS command_lines (
    id_command_line INT AUTO_INCREMENT PRIMARY KEY,
    quantity        INT NOT NULL,
    command_id      INT NOT NULL,
    article_id      INT NOT NULL,
    FOREIGN KEY (command_id) REFERENCES commands(id_command),
    FOREIGN KEY (article_id) REFERENCES articles(id_article)
);

-- ─── Table payments (SINGLE_TABLE : PayPal + CreditCard) ─────────
CREATE TABLE IF NOT EXISTS payments (
    id_payment      INT AUTO_INCREMENT PRIMARY KEY,
    payment_type    VARCHAR(20)  NOT NULL,  -- 'PAYPAL' ou 'CREDIT_CARD'
    amount          DOUBLE       NOT NULL,
    payment_date    DATE         NOT NULL,
    command_id      INT          NOT NULL UNIQUE,
    -- Colonnes PayPal
    account_number  VARCHAR(100),
    -- Colonnes CreditCard
    card_number     VARCHAR(20),
    expiration_date VARCHAR(10),
    FOREIGN KEY (command_id) REFERENCES commands(id_command)
);
