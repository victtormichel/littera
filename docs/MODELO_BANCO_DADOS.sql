-- ====================================================================
-- PROJETO INTEGRADOR TRANSDISCIPLINAR EM CIÊNCIA DA COMPUTAÇÃO
-- SISTEMA LITTERA: CATÁLOGO SIMPLES DE LIVROS
-- AUTOR: Victor Michel Fernandes Leite | RGM: 47117877
-- ARQUIVO: MODELO_BANCO_DADOS.sql
-- DESCRIÇÃO: Scripts DDL e DML para criação e inicialização do banco.
-- ====================================================================

-- -----------------------------------------------------
-- 1. Criação do Banco de Dados (MySQL)
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS littera_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE littera_db;

-- -----------------------------------------------------
-- 2. Tabela: generos
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS generos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- 3. Tabela: livros
-- -----------------------------------------------------
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

-- -----------------------------------------------------
-- 4. Índices para Otimização de Consultas e Buscas
-- -----------------------------------------------------
CREATE INDEX idx_livros_titulo ON livros(titulo);
CREATE INDEX idx_livros_autor ON livros(autor);
CREATE INDEX idx_livros_genero ON livros(genero_id);

-- -----------------------------------------------------
-- 5. Carga de Dados Iniciais (Gêneros Literários)
-- -----------------------------------------------------
INSERT INTO generos (nome) VALUES 
('Romance'),
('Ficção'),
('Terror'),
('Filosofia'),
('Ação'),
('Drama'),
('Aventura'),
('Biografia'),
('Poesia'),
('Fantasia'),
('História');

-- -----------------------------------------------------
-- 6. Carga de Dados Iniciais (Livros de Demonstração)
-- -----------------------------------------------------
INSERT INTO livros (titulo, autor, ano_publicacao, numero_paginas, sinopse, genero_id) VALUES
('Hora de Crescer Interiormente - O mito de Hercules hoje', 'Trigueirinho', 2024, 240, 'Uma reflexão profunda sobre o desenvolvimento interior da consciência e os desafios arquetípicos representados pelos doze trabalhos de Hércules aplicados à vida moderna.', 4),
('Dom Casmurro', 'Machado de Assis', 1899, 256, 'Narrado em primeira pessoa por Bento Santiago, o romance investiga as memórias de sua juventude e o seu amor por Capitu, envolto na clássica dúvida sobre a traição.', 1),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 1943, 96, 'Um piloto cai no deserto do Saara e encontra um jovem príncipe que viaja pelo universo aprendendo sobre a natureza humana e o valor dos laços afetivos.', 2),
('Frankenstein', 'Mary Shelley', 1818, 280, 'A trágica narrativa de Victor Frankenstein, um cientista brilhante que descobre o segredo da vida e cria uma criatura rejeitada pela humanidade.', 3),
('A República', 'Platão', -380, 480, 'Diálogo socrático fundamental que investiga o conceito de justiça, a organização ideal da polis e a célebre alegoria da caverna.', 4);
