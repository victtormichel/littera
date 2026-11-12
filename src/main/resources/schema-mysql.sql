-- ========================================================
-- Projeto Integrador: Littera - Catálogo Simples de Livros
-- Script DDL: MySQL / MariaDB
-- ========================================================

CREATE DATABASE IF NOT EXISTS littera_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE littera_db;

-- Tabela de Gêneros
CREATE TABLE IF NOT EXISTS generos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Livros
CREATE TABLE IF NOT EXISTS livros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    ano_publicacao INT,
    numero_paginas INT,
    sinopse TEXT,
    genero_id INT,
    CONSTRAINT fk_livros_genero FOREIGN KEY (genero_id)
        REFERENCES generos(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para otimização de consultas e busca
CREATE INDEX idx_livros_titulo ON livros(titulo);
CREATE INDEX idx_livros_autor ON livros(autor);
CREATE INDEX idx_livros_genero ON livros(genero_id);
