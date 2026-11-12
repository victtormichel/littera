-- ========================================================
-- Projeto Integrador: Littera - Catálogo Simples de Livros
-- Script DML: Carga de Dados Iniciais
-- ========================================================

-- Carga inicial de gêneros literários
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

-- Carga inicial de livros (Livro de exemplo das telas de referência)
INSERT INTO livros (titulo, autor, ano_publicacao, numero_paginas, sinopse, genero_id) VALUES
('Hora de Crescer Interiormente - O mito de Hercules hoje', 'Trigueirinho', 2024, 240, 'Uma reflexão profunda sobre o desenvolvimento interior da consciência e os desafios arquetípicos representados pelos doze trabalhos de Hércules aplicados à vida moderna.', 4),
('Dom Casmurro', 'Machado de Assis', 1899, 256, 'Narrado em primeira pessoa por Bento Santiago, o romance investiga as memórias de sua juventude e o seu amor por Capitu, envolto na clássica dúvida sobre a traição.', 1),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 1943, 96, 'Um piloto cai no deserto do Saara e encontra um jovem príncipe que viaja pelo universo aprendendo sobre a natureza humana e o valor dos laços afetivos.', 2),
('Frankenstein', 'Mary Shelley', 1818, 280, 'A trágica narrativa de Victor Frankenstein, um cientista brilhante que descobre o segredo da vida e cria uma criatura rejeitada pela humanidade.', 3),
('A República', 'Platão', -380, 480, 'Diálogo socrático fundamental que investiga o conceito de justiça, a organização ideal da polis e a célebre alegoria da caverna.', 4);
