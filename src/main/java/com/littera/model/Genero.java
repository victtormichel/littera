package com.littera.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa a entidade Gênero Literário no sistema Littera.
 * Mapeia a tabela 'generos' do banco de dados relacional.
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class Genero implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador único do gênero (Chave Primária) */
    private Integer id;

    /** Nome descritivo do gênero literário (ex: Ficção, Romance, Filosofia) */
    private String nome;

    /**
     * Construtor padrão (sem argumentos).
     */
    public Genero() {
    }

    /**
     * Construtor com o nome do gênero.
     *
     * @param nome Nome do gênero literário
     */
    public Genero(String nome) {
        this.nome = nome;
    }

    /**
     * Construtor completo com todos os atributos.
     *
     * @param id   Identificador único do gênero
     * @param nome Nome do gênero literário
     */
    public Genero(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // --- Métodos Getters e Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // --- Métodos Sobrescritos (equals, hashCode, toString) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genero genero = (Genero) o;
        return Objects.equals(id, genero.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Genero{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
