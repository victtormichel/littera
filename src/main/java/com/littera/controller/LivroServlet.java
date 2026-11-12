package com.littera.controller;

import com.littera.dao.GeneroDAO;
import com.littera.dao.LivroDAO;
import com.littera.model.Genero;
import com.littera.model.Livro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador Principal do Catálogo Littera.
 * Atua como Servlet Controller (padrão arquitetural MVC), interceptando
 * requisições HTTP, executando regras de negócio e validações, delegando
 * persistência aos DAOs e despachando (forward/redirect) para as páginas JSP (View).
 *
 * Mapeamentos suportados:
 * - /livros
 * - /livros/*
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
@WebServlet(name = "LivroServlet", urlPatterns = {"/livros", "/livros/*"})
public class LivroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LivroServlet.class.getName());

    private LivroDAO livroDAO;
    private GeneroDAO generoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.livroDAO = new LivroDAO();
        this.generoDAO = new GeneroDAO();
        LOGGER.info("LivroServlet inicializado com sucesso.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processarRequisicao(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processarRequisicao(request, response);
    }

    /**
     * Ponto único de entrada e roteamento para requisições GET e POST.
     */
    private void processarRequisicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Garante suporte completo a acentuação e caracteres em português (UTF-8)
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        // Normalização da rota solicitada
        String rota = "listar";
        if (pathInfo != null && !pathInfo.equals("/")) {
            rota = pathInfo.substring(1); // remove a barra inicial
        } else if (action != null && !action.trim().isEmpty()) {
            rota = action.trim();
        }

        LOGGER.fine("Processando rota: " + rota + " | Método: " + request.getMethod());

        try {
            switch (rota) {
                case "novo":
                    exibirFormularioCadastro(request, response);
                    break;
                case "salvar":
                    salvarLivro(request, response);
                    break;
                case "detalhes":
                    exibirDetalhes(request, response);
                    break;
                case "editar":
                    exibirFormularioEdicao(request, response);
                    break;
                case "atualizar":
                    atualizarLivro(request, response);
                    break;
                case "excluir":
                    excluirLivro(request, response);
                    break;
                case "listar":
                default:
                    listarLivros(request, response);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao processar rota '" + rota + "': " + e.getMessage(), e);
            request.setAttribute("erroGeral", "Ocorreu um erro interno no servidor: " + e.getMessage());
            request.getRequestDispatcher("/listar-livros.jsp").forward(request, response);
        }
    }

    /**
     * Lista livros com suporte a busca textual por título/autor e filtro por gênero.
     * Rota: GET /livros ou GET /livros/
     */
    private void listarLivros(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String busca = request.getParameter("q");
        String generoParam = request.getParameter("genero");

        Integer generoId = null;
        if (generoParam != null && !generoParam.trim().isEmpty()) {
            try {
                generoId = Integer.parseInt(generoParam.trim());
            } catch (NumberFormatException ignored) {
                // Parâmetro de gênero inválido, ignora filtro
            }
        }

        // Executa busca com filtros parametrizados
        List<Livro> livros = livroDAO.buscarComFiltro(busca, generoId);
        List<Genero> generos = generoDAO.listarTodos();

        request.setAttribute("livros", livros);
        request.setAttribute("generos", generos);
        request.setAttribute("termoBusca", busca != null ? busca.trim() : "");
        request.setAttribute("generoSelecionado", generoId);
        request.setAttribute("totalLivros", livros.size());

        request.getRequestDispatcher("/listar-livros.jsp").forward(request, response);
    }

    /**
     * Exibe a página de detalhes de um livro específico.
     * Rota: GET /livros/detalhes?id=X
     */
    private void exibirDetalhes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/livros");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            Livro livro = livroDAO.buscarPorId(id);

            if (livro == null) {
                request.getSession().setAttribute("mensagemErro", "Livro não encontrado na coleção.");
                response.sendRedirect(request.getContextPath() + "/livros");
                return;
            }

            request.setAttribute("livro", livro);
            request.getRequestDispatcher("/detalhes-livro.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/livros");
        }
    }

    /**
     * Exibe o formulário de cadastro de um novo livro.
     * Rota: GET /livros/novo
     */
    private void exibirFormularioCadastro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Genero> generos = generoDAO.listarTodos();
        request.setAttribute("generos", generos);
        request.getRequestDispatcher("/adicionar-livro.jsp").forward(request, response);
    }

    /**
     * Processa a submissão do formulário de novo livro com validação de dados.
     * Rota: POST /livros/salvar
     */
    private void salvarLivro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String generoIdStr = request.getParameter("genero_id");
        String anoPublicacaoStr = request.getParameter("ano_publicacao");
        String numeroPaginasStr = request.getParameter("numero_paginas");
        String sinopse = request.getParameter("sinopse");

        // --- Validações de Entrada (Segurança da Informação e Integridade) ---
        StringBuilder erros = new StringBuilder();

        if (titulo == null || titulo.trim().isEmpty()) {
            erros.append("O título do livro é obrigatório. ");
        } else if (titulo.trim().length() > 255) {
            erros.append("O título não pode exceder 255 caracteres. ");
        }

        if (autor == null || autor.trim().isEmpty()) {
            erros.append("O nome do(a) autor(a) é obrigatório. ");
        } else if (autor.trim().length() > 150) {
            erros.append("O autor não pode exceder 150 caracteres. ");
        }

        Integer generoId = null;
        if (generoIdStr == null || generoIdStr.trim().isEmpty()) {
            erros.append("Selecione um gênero válido. ");
        } else {
            try {
                generoId = Integer.parseInt(generoIdStr.trim());
            } catch (NumberFormatException e) {
                erros.append("Gênero selecionado inválido. ");
            }
        }

        Integer anoPublicacao = null;
        if (anoPublicacaoStr != null && !anoPublicacaoStr.trim().isEmpty()) {
            try {
                anoPublicacao = Integer.parseInt(anoPublicacaoStr.trim());
                if (anoPublicacao < -3000 || anoPublicacao > 2100) {
                    erros.append("Ano de publicação fora do intervalo válido. ");
                }
            } catch (NumberFormatException e) {
                erros.append("O ano de publicação deve ser um número inteiro. ");
            }
        }

        Integer numeroPaginas = null;
        if (numeroPaginasStr != null && !numeroPaginasStr.trim().isEmpty()) {
            try {
                numeroPaginas = Integer.parseInt(numeroPaginasStr.trim());
                if (numeroPaginas <= 0 || numeroPaginas > 100000) {
                    erros.append("O número de páginas deve ser um valor positivo razoável. ");
                }
            } catch (NumberFormatException e) {
                erros.append("O número de páginas deve ser um número inteiro. ");
            }
        }

        // Se houver erros, retorna para o formulário preservando os dados digitados
        if (erros.length() > 0) {
            request.setAttribute("erroValidacao", erros.toString().trim());
            Livro rascunho = new Livro(
                    titulo, autor, anoPublicacao, numeroPaginas, sinopse, generoId
            );
            request.setAttribute("livro", rascunho);
            request.setAttribute("generos", generoDAO.listarTodos());
            request.getRequestDispatcher("/adicionar-livro.jsp").forward(request, response);
            return;
        }

        // Cria e persiste o livro
        Livro novoLivro = new Livro();
        novoLivro.setTitulo(titulo.trim());
        novoLivro.setAutor(autor.trim());
        novoLivro.setGeneroId(generoId);
        novoLivro.setAnoPublicacao(anoPublicacao);
        novoLivro.setNumeroPaginas(numeroPaginas);
        novoLivro.setSinopse(sinopse != null ? sinopse.trim() : null);

        boolean sucesso = livroDAO.inserir(novoLivro);

        if (sucesso) {
            request.getSession().setAttribute("mensagemSucesso", "Livro adicionado com sucesso à coleção!");
            response.sendRedirect(request.getContextPath() + "/livros/detalhes?id=" + novoLivro.getId());
        } else {
            request.setAttribute("erroValidacao", "Falha ao salvar o livro no banco de dados.");
            request.setAttribute("livro", novoLivro);
            request.setAttribute("generos", generoDAO.listarTodos());
            request.getRequestDispatcher("/adicionar-livro.jsp").forward(request, response);
        }
    }

    /**
     * Exibe o formulário preenchido para edição de um livro existente.
     * Rota: GET /livros/editar?id=X
     */
    private void exibirFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/livros");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            Livro livro = livroDAO.buscarPorId(id);

            if (livro == null) {
                request.getSession().setAttribute("mensagemErro", "Livro não encontrado para edição.");
                response.sendRedirect(request.getContextPath() + "/livros");
                return;
            }

            request.setAttribute("livro", livro);
            request.setAttribute("generos", generoDAO.listarTodos());
            request.getRequestDispatcher("/editar-livro.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/livros");
        }
    }

    /**
     * Processa a atualização dos dados de um livro.
     * Rota: POST /livros/atualizar
     */
    private void atualizarLivro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/livros");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/livros");
            return;
        }

        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String generoIdStr = request.getParameter("genero_id");
        String anoPublicacaoStr = request.getParameter("ano_publicacao");
        String numeroPaginasStr = request.getParameter("numero_paginas");
        String sinopse = request.getParameter("sinopse");

        // --- Validações de Entrada ---
        StringBuilder erros = new StringBuilder();

        if (titulo == null || titulo.trim().isEmpty()) {
            erros.append("O título do livro é obrigatório. ");
        } else if (titulo.trim().length() > 255) {
            erros.append("O título não pode exceder 255 caracteres. ");
        }

        if (autor == null || autor.trim().isEmpty()) {
            erros.append("O autor é obrigatório. ");
        } else if (autor.trim().length() > 150) {
            erros.append("O autor não pode exceder 150 caracteres. ");
        }

        Integer generoId = null;
        if (generoIdStr == null || generoIdStr.trim().isEmpty()) {
            erros.append("Selecione um gênero válido. ");
        } else {
            try {
                generoId = Integer.parseInt(generoIdStr.trim());
            } catch (NumberFormatException e) {
                erros.append("Gênero selecionado inválido. ");
            }
        }

        Integer anoPublicacao = null;
        if (anoPublicacaoStr != null && !anoPublicacaoStr.trim().isEmpty()) {
            try {
                anoPublicacao = Integer.parseInt(anoPublicacaoStr.trim());
                if (anoPublicacao < -3000 || anoPublicacao > 2100) {
                    erros.append("Ano de publicação fora do intervalo válido. ");
                }
            } catch (NumberFormatException e) {
                erros.append("O ano de publicação deve ser um número inteiro. ");
            }
        }

        Integer numeroPaginas = null;
        if (numeroPaginasStr != null && !numeroPaginasStr.trim().isEmpty()) {
            try {
                numeroPaginas = Integer.parseInt(numeroPaginasStr.trim());
                if (numeroPaginas <= 0 || numeroPaginas > 100000) {
                    erros.append("O número de páginas deve ser um valor positivo razoável. ");
                }
            } catch (NumberFormatException e) {
                erros.append("O número de páginas deve ser um número inteiro. ");
            }
        }

        if (erros.length() > 0) {
            request.setAttribute("erroValidacao", erros.toString().trim());
            Livro rascunho = new Livro(id, titulo, autor, anoPublicacao, numeroPaginas, sinopse, generoId, null);
            request.setAttribute("livro", rascunho);
            request.setAttribute("generos", generoDAO.listarTodos());
            request.getRequestDispatcher("/editar-livro.jsp").forward(request, response);
            return;
        }

        Livro livro = new Livro();
        livro.setId(id);
        livro.setTitulo(titulo.trim());
        livro.setAutor(autor.trim());
        livro.setGeneroId(generoId);
        livro.setAnoPublicacao(anoPublicacao);
        livro.setNumeroPaginas(numeroPaginas);
        livro.setSinopse(sinopse != null ? sinopse.trim() : null);

        boolean sucesso = livroDAO.atualizar(livro);

        if (sucesso) {
            request.getSession().setAttribute("mensagemSucesso", "Informações do livro atualizadas com sucesso!");
            response.sendRedirect(request.getContextPath() + "/livros/detalhes?id=" + id);
        } else {
            request.setAttribute("erroValidacao", "Erro ao atualizar livro no banco de dados.");
            request.setAttribute("livro", livro);
            request.setAttribute("generos", generoDAO.listarTodos());
            request.getRequestDispatcher("/editar-livro.jsp").forward(request, response);
        }
    }

    /**
     * Remove um livro do catálogo.
     * Rota: POST ou GET /livros/excluir?id=X
     */
    private void excluirLivro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr.trim());
                boolean sucesso = livroDAO.excluir(id);
                if (sucesso) {
                    request.getSession().setAttribute("mensagemSucesso", "Livro excluído com sucesso do catálogo.");
                } else {
                    request.getSession().setAttribute("mensagemErro", "Não foi possível excluir o livro.");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("mensagemErro", "Identificador de livro inválido.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/livros");
    }
}
