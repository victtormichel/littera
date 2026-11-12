package com.littera;

import com.littera.model.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes Unitários de validações e regras de negócio para o modelo Livro.
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class ValidacaoLivroTest {

    @Test
    @DisplayName("Deve formatar corretamente valores nulos como 'Não informado'")
    public void testFormatacaoCamposNulos() {
        Livro livro = new Livro();
        livro.setTitulo("Livro Sem Detalhes");
        livro.setAutor("Autor Anônimo");

        assertEquals("Não informado", livro.getAnoPublicacaoFormatado());
        assertEquals("Não informado", livro.getNumeroPaginasFormatado());
        assertEquals("Não informado", livro.getGeneroNomeFormatado());
    }

    @Test
    @DisplayName("Deve formatar corretamente valores preenchidos")
    public void testFormatacaoCamposPreenchidos() {
        Livro livro = new Livro();
        livro.setAnoPublicacao(2024);
        livro.setNumeroPaginas(240);
        livro.setGeneroNome("Filosofia");

        assertEquals("2024", livro.getAnoPublicacaoFormatado());
        assertEquals("240", livro.getNumeroPaginasFormatado());
        assertEquals("Filosofia", livro.getGeneroNomeFormatado());
    }

    @Test
    @DisplayName("Deve validar igualdade baseada no ID (equals e hashCode)")
    public void testEqualsAndHashCode() {
        Livro l1 = new Livro();
        l1.setId(10);
        l1.setTitulo("Título A");

        Livro l2 = new Livro();
        l2.setId(10);
        l2.setTitulo("Título B");

        Livro l3 = new Livro();
        l3.setId(20);

        assertEquals(l1, l2, "Livros com o mesmo ID devem ser considerados iguais.");
        assertEquals(l1.hashCode(), l2.hashCode());
        assertNotEquals(l1, l3, "Livros com IDs distintos não devem ser iguais.");
    }
}
