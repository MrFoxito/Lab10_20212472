CREATE DATABASE IF NOT EXISTS buscaminas_db;
USE buscaminas_db;

CREATE TABLE configuracion (
    idMina BIGINT AUTO_INCREMENT PRIMARY KEY,
    dimMinaX INT NOT NULL,
    dimMinaY INT NOT NULL,
    cantBombas INT NOT NULL,
    cantIntentos INT NOT NULL,
    cantIntentosActual INT DEFAULT 0
);

CREATE TABLE posicionbomba (
    idBomba BIGINT AUTO_INCREMENT PRIMARY KEY,
    coordenadaX INT NOT NULL,
    coordenadaY INT NOT NULL,
    idMina BIGINT NOT NULL,
    FOREIGN KEY (idMina) REFERENCES configuracion(idMina) ON DELETE CASCADE
);

CREATE TABLE movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coordenadaX INT NOT NULL,
    coordenadaY INT NOT NULL,
    idMina BIGINT NOT NULL,
    esBomba BOOLEAN DEFAULT FALSE,
    descubierta BOOLEAN DEFAULT FALSE,
    numeroVecinas INT DEFAULT 0,
    FOREIGN KEY (idMina) REFERENCES configuracion(idMina) ON DELETE CASCADE
);