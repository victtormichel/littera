package com.littera.dao;

import com.littera.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) para a entidade Livro.
 * Encapsula todas as operações de persistência (CRUD), pesquisa textual e
 * filtragem por gênero na tabela 'livros', realizando JOIN com 'generos'.
 *
 * Aplica rigorosamente o uso de PreparedStatements como mecanismo de defesa
 * contra ataques de SQL Injection, em conformidade com as boas práticas
 * da engenharia de software e segurança da informação (ISO/IEC 27001).
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class LivroDAO {

    private static final Logger LOGGER = Logger.getLogger(LivroDAO.class.getName());

    /**
     * Mapeia uma linha do ResultSet para uma instância do modelo Livro.
     *
     * @param rs ResultSet posicionado no registro atual
     * @return Objeto Livro populado
     * @throws SQLException Em caso de erro na leitura das colunas
     */
    private Livro mapearLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));

        int ano = rs.getInt("ano_publicacao");
        livro.setAnoPublicacao(rs.wasNull() ? null : ano);

        int paginas = rs.getInt("numero_paginas");
        livro.setNumeroPaginas(rs.wasNull() ? null : paginas);

        livro.setSinopse(rs.getString("sinopse"));

        int generoId = rs.getInt("genero_id");
        livro.setGeneroId(rs.wasNull() ? null : generoId);

        // Nome do gênero obtido através do LEFT JOIN
        livro.setGeneroNome(rs.getString("genero_nome"));

        return livro;
    }

    /**
     * Lista todos os livros catalogados, ordenados de forma decrescente por ID.
     *
     * @return Lista de livros cadastrados
     */
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT l.id, l.titulo, l.autor, l.ano_publicacao, l.numero_paginas, " +
                     "       l.sinopse, l.genero_id, g.nome AS genero_nome " +
                     "FROM livros l " +
                     "LEFT JOIN generos g ON l.genero_id = g.id " +
                     "ORDER BY l.id DESC";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearLivro(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todos os livros: " + e.getMessage(), e);
        }

        return lista;
    }

    /**
     * Busca um livro específico pelo seu identificador primário (ID).
     *
     * @param id Identificador do livro
     * @return Objeto Livro se encontrado, ou null se inexistente
     */
    public Livro buscarPorId(int id) {
        String sql = "SELECT l.id, l.titulo, l.autor, l.ano_publicacao, l.numero_paginas, " +
                     "       l.sinopse, l.genero_id, g.nome AS genero_nome " +
                     "FROM livros l " +
                     "LEFT JOIN generos g ON l.genero_id = g.id " +
                     "WHERE l.id = ?";
        Livro livro = null;

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    livro = mapearLivro(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar livro por ID " + id + ": " + e.getMessage(), e);
        }

        return livro;
    }

    /**
     * Realiza uma busca textual simples por título ou autor.
     * Utiliza PreparedStatements com parâmetros parametrizados para garantir
     * imunidade total a SQL Injection.
     *
     * @param termo Palavra ou trecho a ser pesquisado
     * @return Lista de livros correspondentes ao critério de busca
     */
    public List<Livro> buscarPorTermo(String termo) {
        return buscarComFiltro(termo, null);
    }

    /**
     * Lista livros filtrados por uma categoria/gênero específico.
     *
     * @param generoId ID do gênero literário
     * @return Lista de livros pertencentes ao gênero
     */
    public List<Livro> listarPorGenero(int generoId) {
        return buscarComFiltro(null, generoId);
    }

    /**
     * Realiza busca combinada com termo textual (título ou autor) e/ou gênero literário.
     *
     * @param termo    Termo para busca em título/autor (pode ser nulo ou vazio)
     * @param generoId ID do gênero para filtro (pode ser nulo para ignorar filtro de gênero)
     * @return Lista de livros resultantes da consulta
     */
    public List<Livro> buscarComFiltro(String termo, Integer generoId) {
        List<Livro> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l.id, l.titulo, l.autor, l.ano_publicacao, l.numero_paginas, ")
           .append("       l.sinopse, l.genero_id, g.nome AS genero_nome ")
           .append("FROM livros l ")
           .append("LEFT JOIN generos g ON l.genero_id = g.id ")
           .append("WHERE 1=1 ");

        boolean temTermo = termo != null && !termo.trim().isEmpty();
        boolean temGenero = generoId != null && generoId > 0;

        if (temTermo) {
            sql.append("AND (LOWER(l.titulo) LIKE ? OR LOWER(l.autor) LIKE ?) ");
        }

        if (temGenero) {
            sql.append("AND l.genero_id = ? ");
        }

        sql.append("ORDER BY l.id DESC");

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (temTermo) {
                String pattern = "%" + termo.trim().toLowerCase() + "%";
                stmt.setString(paramIndex++, pattern);
                stmt.setString(paramIndex++, pattern);
            }

            if (temGenero) {
                stmt.setInt(paramIndex++, generoId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearLivro(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao filtrar livros: " + e.getMessage(), e);
        }

        return lista;
    }

    /**
     * Insere um novo registro de livro no banco de dados.
     *
     * @param livro Instância de Livro contendo os dados a serem cadastrados
     * @return true se a inserção foi bem-sucedida, false em caso de falha
     */
    public boolean inserir(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, ano_publicacao, numero_paginas, sinopse, genero_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, livro.getTitulo().trim());
            stmt.setString(2, livro.getAutor().trim());

            if (livro.getAnoPublicacao() != null) {
                stmt.setInt(3, livro.getAnoPublicacao());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (livro.getNumeroPaginas() != null) {
                stmt.setInt(4, livro.getNumeroPaginas());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (livro.getSinopse() != null && !livro.getSinopse().trim().isEmpty()) {
                stmt.setString(5, livro.getSinopse().trim());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            if (livro.getGeneroId() != null && livro.getGeneroId() > 0) {
                stmt.setInt(6, livro.getGeneroId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livro.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar livro: " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Atualiza as informações de um livro existente no banco de dados.
     *
     * @param livro Objeto Livro com dados atualizados e ID preenchido
     * @return true se ao menos uma linha foi atualizada, false caso contrário
     */
    public boolean atualizar(Livro livro) {
        String sql = "UPDATE livros SET titulo = ?, autor = ?, ano_publicacao = ?, " +
                     "numero_paginas = ?, sinopse = ?, genero_id = ? WHERE id = ?";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo().trim());
            stmt.setString(2, livro.getAutor().trim());

            if (livro.getAnoPublicacao() != null) {
                stmt.setInt(3, livro.getAnoPublicacao());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            if (livro.getNumeroPaginas() != null) {
                stmt.setInt(4, livro.getNumeroPaginas());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (livro.getSinopse() != null && !livro.getSinopse().trim().isEmpty()) {
                stmt.setString(5, livro.getSinopse().trim());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            if (livro.getGeneroId() != null && livro.getGeneroId() > 0) {
                stmt.setInt(6, livro.getGeneroId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, livro.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar livro ID " + livro.getId() + ": " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Remove um livro do catálogo pelo seu identificador primário.
     *
     * @param id Identificador do livro
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM livros WHERE id = ?";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir livro ID " + id + ": " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Retorna a quantidade total de livros cadastrados.
     *
     * @return Total de registros na tabela livros
     */
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM livros";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao contar total de livros: " + e.getMessage(), e);
        }

        return 0;
    }
}
