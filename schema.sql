-- Script SQL pour créer la base de données formation_db
-- À exécuter sur la VM MariaDB

CREATE DATABASE IF NOT EXISTS formation_db;
USE formation_db;

-- Table employes
CREATE TABLE IF NOT EXISTS employes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM('EMPLOYE', 'RESPONSABLE') NOT NULL
);

-- Table formations
CREATE TABLE IF NOT EXISTS formations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    description TEXT,
    duree_jours INT NOT NULL
);

-- Table demandes
CREATE TABLE IF NOT EXISTS demandes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employe_id BIGINT NOT NULL,
    formation_id BIGINT NOT NULL,
    statut ENUM('EN_ATTENTE', 'ACCEPTEE', 'REFUSEE') NOT NULL,
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employe_id) REFERENCES employes(id),
    FOREIGN KEY (formation_id) REFERENCES formations(id)
);

-- Table sessions
CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    formation_id BIGINT NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    FOREIGN KEY (formation_id) REFERENCES formations(id)
);

-- Table evaluations
CREATE TABLE IF NOT EXISTS evaluations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    demande_id BIGINT NOT NULL,
    note INT CHECK (note >= 1 AND note <= 5),
    commentaire TEXT,
    date_evaluation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (demande_id) REFERENCES demandes(id)
);

-- Insérer des données de test
INSERT INTO employes (nom, email, mot_de_passe, role) VALUES
('Erica Toure', 'erica.toure@entreprise.com', 'password123', 'EMPLOYE'),
('Fabrice Dupont', 'fabrice.dupont@entreprise.com', 'password123', 'EMPLOYE'),
('Michael Koffi', 'michael.koffi@entreprise.com', 'password123', 'EMPLOYE'),
('Responsable RH', 'rh@entreprise.com', 'admin123', 'RESPONSABLE');

INSERT INTO formations (titre, description, duree_jours) VALUES
('Java Avancé', 'Formation approfondie sur Java Spring Boot', 5),
('Anglais Professionnel', 'Améliorer les compétences en anglais', 10),
('Python pour Data Science', 'Introduction à Python et analyse de données', 7);

INSERT INTO demandes (employe_id, formation_id, statut) VALUES
(1, 1, 'ACCEPTEE'),
(1, 2, 'EN_ATTENTE'),
(2, 3, 'REFUSEE'),
(3, 1, 'ACCEPTEE');

INSERT INTO sessions (formation_id, date_debut, date_fin) VALUES
(1, '2025-06-01', '2025-06-05'),
(2, '2025-07-01', '2025-07-10'),
(3, '2025-08-01', '2025-08-07');

INSERT INTO evaluations (demande_id, note, commentaire) VALUES
(1, 5, 'Excellente formation, très utile'),
(4, 4, 'Bon contenu, mais un peu rapide');