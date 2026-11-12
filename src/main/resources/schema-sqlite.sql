-- ========================================================
-- Projeto Integrador: Littera - Catálogo Simples de Livros
-- Script DDL: SQLite (Embedded)
-- ========================================================

-- Tabela de Gêneros
CREATE TABLE IF NOT EXISTS generos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL UNIQUE
);

-- Tabela de Livros
CREATE TABLE IF NOT EXISTS livros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    autor TEXT NOT NULL,
    ano_publicacao INTEGER,
    numero_paginas INTEGER,
    sinopse TEXT,
    genero_id INTEGER,
    FOREIGN KEY (genero_id) REFERENCES generos(id) ON DELETE SET NULL
);

-- Índices para otimização de consultas e busca
CREATE INDEX IF NOT EXISTS idx_livros_titulo ON livros(titulo);
CREATE INDEX IF NOT EXISTS idx_livros_autor ON livros(autor);
CREATE INDEX IF NOT EXISTS idx_livros_genero ON livros(genero_id);
