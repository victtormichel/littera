package com.littera;

import com.littera.dao.FabricaDeConexoes;
import com.littera.dao.GeneroDAO;
import com.littera.dao.LivroDAO;
import com.littera.model.Genero;
import com.littera.model.Livro;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários e de Integração para a camada de persistência (LivroDAO e GeneroDAO).
 * Valida o ciclo completo do CRUD, a integridade referencial e os métodos de busca com SQL.
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LivroDAOTest {

    private static LivroDAO livroDAO;
    private static GeneroDAO generoDAO;
    private static int idLivroTeste;
    private static int idGeneroTeste;

    @BeforeAll
    public static void configurar() throws SQLException {
        // Assegura conexão ativa e tabelas criadas
        Connection conn = FabricaDeConexoes.getConexao();
        assertNotNull(conn, "A conexão com o banco de dados não deve ser nula.");
        conn.close();

        livroDAO = new LivroDAO();
        generoDAO = new GeneroDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Deve listar os gêneros cadastrados com sucesso")
    public void testListarGeneros() {
        List<Genero> generos = generoDAO.listarTodos();
        assertNotNull(generos, "A lista de gêneros não pode ser nula.");
        assertFalse(generos.isEmpty(), "Deve haver gêneros pré-populados na base de dados.");
        idGeneroTeste = generos.get(0).getId();
        assertTrue(idGeneroTeste > 0, "O ID do gênero deve ser positivo.");
    }

    @Test
    @Order(2)
    @DisplayName("Deve cadastrar um novo livro com sucesso (Create)")
    public void testInserirLivro() {
        Livro livro = new Livro();
        livro.setTitulo("Ensaio sobre a Cegueira");
        livro.setAutor("José Saramago");
        livro.setAnoPublicacao(1995);
        livro.setNumeroPaginas(312);
        livro.setSinopse("Uma terrível epidemia de cegueira branca se espalha pela cidade.");
        livro.setGeneroId(idGeneroTeste);

        boolean sucesso = livroDAO.inserir(livro);
        assertTrue(sucesso, "A inserção do livro deve retornar true.");
        assertNotNull(livro.getId(), "O ID gerado pelo banco não deve ser nulo.");
        assertTrue(livro.getId() > 0, "O ID gerado deve ser maior que zero.");

        idLivroTeste = livro.getId();
    }

    @Test
    @Order(3)
    @DisplayName("Deve consultar o livro cadastrado por ID (Read)")
    public void testBuscarPorId() {
        Livro livro = livroDAO.buscarPorId(idLivroTeste);
        assertNotNull(livro, "O livro cadastrado deve ser encontrado pelo ID.");
        assertEquals("Ensaio sobre a Cegueira", livro.getTitulo(), "O título deve corresponder ao cadastrado.");
        assertEquals("José Saramago", livro.getAutor(), "O autor deve corresponder.");
        assertEquals(1995, livro.getAnoPublicacao());
        assertEquals(312, livro.getNumeroPaginas());
        assertNotNull(livro.getGeneroNome(), "O nome do gênero deve ser populado via JOIN.");
    }

    @Test
    @Order(4)
    @DisplayName("Deve buscar livro por termo de pesquisa no título ou autor (Search)")
    public void testBuscarPorTermo() {
        // Busca por trecho do autor
        List<Livro> resultadoAutor = livroDAO.buscarPorTermo("Saramago");
        assertFalse(resultadoAutor.isEmpty(), "A busca por 'Saramago' deve retornar resultados.");
        assertTrue(resultadoAutor.stream().anyMatch(l -> l.getId() == idLivroTeste));

        // Busca por trecho do título
        List<Livro> resultadoTitulo = livroDAO.buscarPorTermo("Cegueira");
        assertFalse(resultadoTitulo.isEmpty(), "A busca por 'Cegueira' deve retornar resultados.");
        assertTrue(resultadoTitulo.stream().anyMatch(l -> l.getId() == idLivroTeste));
    }

    @Test
    @Order(5)
    @DisplayName("Deve atualizar as informações do livro (Update)")
    public void testAtualizarLivro() {
        Livro livro = livroDAO.buscarPorId(idLivroTeste);
        assertNotNull(livro);

        livro.setTitulo("Ensaio sobre a Cegueira - Edição Especial");
        livro.setNumeroPaginas(320);

        boolean atualizado = livroDAO.atualizar(livro);
        assertTrue(atualizado, "A atualização do livro deve retornar true.");

        Livro livroAtualizado = livroDAO.buscarPorId(idLivroTeste);
        assertEquals("Ensaio sobre a Cegueira - Edição Especial", livroAtualizado.getTitulo());
        assertEquals(320, livroAtualizado.getNumeroPaginas());
    }

    @Test
    @Order(6)
    @DisplayName("Deve excluir o livro do catálogo (Delete)")
    public void testExcluirLivro() {
        boolean excluido = livroDAO.excluir(idLivroTeste);
        assertTrue(excluido, "A exclusão do livro deve retornar true.");

        Livro livroPosExclusao = livroDAO.buscarPorId(idLivroTeste);
        assertNull(livroPosExclusao, "O livro não deve mais existir no banco de dados.");
    }
}
