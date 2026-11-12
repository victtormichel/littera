package com.littera.dao;

import com.littera.model.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) para a entidade Genero.
 * Responsável por todas as operações de persistência (CRUD) da tabela 'generos',
 * utilizando JDBC e PreparedStatements para garantir a integridade e segurança.
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class GeneroDAO {

    private static final Logger LOGGER = Logger.getLogger(GeneroDAO.class.getName());

    /**
     * Lista todos os gêneros literários cadastrados no banco de dados.
     *
     * @return Lista contendo todos os gêneros ordenados por nome
     */
    public List<Genero> listarTodos() {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM generos ORDER BY nome ASC";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setId(rs.getInt("id"));
                genero.setNome(rs.getString("nome"));
                lista.add(genero);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar todos os gêneros: " + e.getMessage(), e);
        }

        return lista;
    }

    /**
     * Busca um gênero pelo seu identificador primário.
     *
     * @param id ID do gênero
     * @return Objeto Genero se encontrado, ou null
     */
    public Genero buscarPorId(int id) {
        String sql = "SELECT id, nome FROM generos WHERE id = ?";
        Genero genero = null;

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    genero = new Genero();
                    genero.setId(rs.getInt("id"));
                    genero.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar gênero por ID " + id + ": " + e.getMessage(), e);
        }

        return genero;
    }

    /**
     * Busca um gênero pelo seu nome exato (case-insensitive).
     *
     * @param nome Nome do gênero
     * @return Objeto Genero se encontrado, ou null
     */
    public Genero buscarPorNome(String nome) {
        String sql = "SELECT id, nome FROM generos WHERE LOWER(nome) = LOWER(?)";
        Genero genero = null;

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    genero = new Genero();
                    genero.setId(rs.getInt("id"));
                    genero.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar gênero por nome: " + e.getMessage(), e);
        }

        return genero;
    }

    /**
     * Insere um novo gênero no banco de dados.
     *
     * @param genero Objeto Genero a ser inserido
     * @return true se inserido com sucesso, false caso contrário
     */
    public boolean inserir(Genero genero) {
        String sql = "INSERT INTO generos (nome) VALUES (?)";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, genero.getNome());
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        genero.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir gênero: " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Atualiza os dados de um gênero existente.
     *
     * @param genero Objeto Genero com id e novo nome
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean atualizar(Genero genero) {
        String sql = "UPDATE generos SET nome = ? WHERE id = ?";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genero.getNome());
            stmt.setInt(2, genero.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar gênero: " + e.getMessage(), e);
        }

        return false;
    }

    /**
     * Exclui um gênero pelo identificador.
     *
     * @param id Identificador do gênero
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM generos WHERE id = ?";

        try (Connection conn = FabricaDeConexoes.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir gênero ID " + id + ": " + e.getMessage(), e);
        }

        return false;
    }
}
