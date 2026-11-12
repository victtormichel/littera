package com.littera.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa a entidade Livro no sistema Littera.
 * Mapeia a tabela 'livros' do banco de dados relacional e inclui
 * o relacionamento com a tabela 'generos' via foreign key (genero_id).
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador único do livro (Chave Primária) */
    private Integer id;

    /** Título do livro (obrigatório, até 255 caracteres) */
    private String titulo;

    /** Nome do(a) autor(a) da obra (obrigatório, até 150 caracteres) */
    private String autor;

    /** Ano de publicação ou lançamento da edição */
    private Integer anoPublicacao;

    /** Quantidade total de páginas do livro */
    private Integer numeroPaginas;

    /** Breve sinopse ou resumo da obra literária */
    private String sinopse;

    /** Chave estrangeira referenciando a tabela 'generos' */
    private Integer generoId;

    /** Campo auxiliar para exibição do nome do gênero associado */
    private String generoNome;

    /**
     * Construtor padrão (sem argumentos).
     */
    public Livro() {
    }

    /**
     * Construtor com campos essenciais.
     *
     * @param titulo Título do livro
     * @param autor  Nome do autor
     */
    public Livro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    /**
     * Construtor para cadastro com atributos completos (sem id).
     *
     * @param titulo        Título do livro
     * @param autor         Nome do autor
     * @param anoPublicacao Ano de publicação
     * @param numeroPaginas Número de páginas
     * @param sinopse       Sinopse do livro
     * @param generoId      ID do gênero
     */
    public Livro(String titulo, String autor, Integer anoPublicacao, Integer numeroPaginas, String sinopse, Integer generoId) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroPaginas = numeroPaginas;
        this.sinopse = sinopse;
        this.generoId = generoId;
    }

    /**
     * Construtor completo com ID e nome do gênero.
     *
     * @param id            Identificador único
     * @param titulo        Título do livro
     * @param autor         Nome do autor
     * @param anoPublicacao Ano de publicação
     * @param numeroPaginas Número de páginas
     * @param sinopse       Sinopse do livro
     * @param generoId      ID do gênero
     * @param generoNome    Nome descritivo do gênero
     */
    public Livro(Integer id, String titulo, String autor, Integer anoPublicacao, Integer numeroPaginas, String sinopse, Integer generoId, String generoNome) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.numeroPaginas = numeroPaginas;
        this.sinopse = sinopse;
        this.generoId = generoId;
        this.generoNome = generoNome;
    }

    // --- Métodos Getters e Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public Integer getGeneroId() {
        return generoId;
    }

    public void setGeneroId(Integer generoId) {
        this.generoId = generoId;
    }

    public String getGeneroNome() {
        return generoNome;
    }

    public void setGeneroNome(String generoNome) {
        this.generoNome = generoNome;
    }

    // --- Métodos de Conveniência para a View ---

    /**
     * Retorna o ano de publicação formatado ou mensagem padrão se nulo.
     *
     * @return Ano ou "Não informado"
     */
    public String getAnoPublicacaoFormatado() {
        return (anoPublicacao != null && anoPublicacao != 0) ? String.valueOf(anoPublicacao) : "Não informado";
    }

    /**
     * Retorna o número de páginas formatado ou mensagem padrão se nulo.
     *
     * @return Páginas ou "Não informado"
     */
    public String getNumeroPaginasFormatado() {
        return (numeroPaginas != null && numeroPaginas > 0) ? String.valueOf(numeroPaginas) : "Não informado";
    }

    /**
     * Retorna o nome do gênero ou "Não informado" se nulo.
     *
     * @return Gênero ou "Não informado"
     */
    public String getGeneroNomeFormatado() {
        return (generoNome != null && !generoNome.trim().isEmpty()) ? generoNome : "Não informado";
    }

    // --- Métodos Sobrescritos ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", numeroPaginas=" + numeroPaginas +
                ", generoId=" + generoId +
                ", generoNome='" + generoNome + '\'' +
                '}';
    }
}
