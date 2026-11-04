-- Base de datos para Gestor de clientes, servicios y reservas
-- Ejecutar este script en MySQL Workbench.

CREATE DATABASE IF NOT EXISTS gestor_reservas_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE gestor_reservas_db;

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    PRIMARY KEY (id),
    UNIQUE KEY uk_clientes_email (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS servicios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fecha_reserva DATE NOT NULL,
    estado VARCHAR(255) NOT NULL,
    cliente_id BIGINT NOT NULL,
    servicio_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_reservas_cliente_id (cliente_id),
    KEY idx_reservas_servicio_id (servicio_id),
    CONSTRAINT fk_reservas_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_reservas_servicio
        FOREIGN KEY (servicio_id) REFERENCES servicios (id)
) ENGINE=InnoDB;
