<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Littera - Adicionar Novo Livro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Barra de Navegação Lateral -->
    <aside class="sidebar">
        <a href="${pageContext.request.contextPath}/livros" class="brand-container" title="Littera Home">
            <span class="brand-logo-letter">L.</span>
            <span class="brand-name">LITTERA</span>
        </a>

        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/livros" class="nav-item" title="Coleção de Livros">
                <svg viewBox="0 0 24 24">
                    <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path>
                    <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path>
                    <line x1="9" y1="6" x2="9" y2="13"></line>
                </svg>
            </a>
        </nav>
    </aside>

    <!-- Conteúdo Principal -->
    <main class="main-wrapper">

        <!-- Linha Superior de Navegação -->
        <div class="top-navigation-row">
            <a href="${pageContext.request.contextPath}/livros" class="back-link">
                <!-- Seta para a esquerda -->
                <svg viewBox="0 0 24 24">
                    <line x1="19" y1="12" x2="5" y2="12"></line>
                    <polyline points="12 19 5 12 12 5"></polyline>
                </svg>
                Voltar à coleção
            </a>
        </div>

        <h1 class="page-title">Adicionar novo livro</h1>
        <p class="page-subtitle">Preencha os dados do livro para adicioná-lo à sua coleção.</p>

        <!-- Feedback de Validação -->
        <c:if test="${not empty erroValidacao}">
            <div class="alert-feedback alert-erro">
                <span><c:out value="${erroValidacao}"/></span>
            </div>
        </c:if>

        <!-- Formulário de Cadastro -->
        <form action="${pageContext.request.contextPath}/livros/salvar" method="POST" id="form-livro" class="form-container">

            <!-- Linha 1: Título e Autor -->
            <div class="form-row">
                <div class="form-group col-2">
                    <label for="titulo" class="form-label">Título do livro *</label>
                    <input type="text"
                           id="titulo"
                           name="titulo"
                           class="form-input"
                           placeholder="Digite o título do livro"
                           value="<c:out value="${livro.titulo}"/>"
                           required
                           maxlength="255">
                </div>

                <div class="form-group col-2">
                    <label for="autor" class="form-label">Autor *</label>
                    <input type="text"
                           id="autor"
                           name="autor"
                           class="form-input"
                           placeholder="Digite o nome do autor"
                           value="<c:out value="${livro.autor}"/>"
                           required
                           maxlength="150">
                </div>
            </div>

            <!-- Linha 2: Gênero, Ano e Páginas -->
            <div class="form-row">
                <div class="form-group col-3">
                    <label for="genero_id" class="form-label">Gênero *</label>
                    <select id="genero_id" name="genero_id" class="form-select" required>
                        <option value="" disabled <c:if test="${empty livro.generoId}">selected</c:if>>Selecione o gênero</option>
                        <c:forEach var="gen" items="${generos}">
                            <option value="${gen.id}" <c:if test="${livro.generoId == gen.id}">selected</c:if>>
                                <c:out value="${gen.nome}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group col-3">
                    <label for="ano_publicacao" class="form-label">Ano de publicação</label>
                    <input type="number"
                           id="ano_publicacao"
                           name="ano_publicacao"
                           class="form-input"
                           placeholder="Ex.: 2024"
                           value="<c:out value="${livro.anoPublicacao}"/>"
                           min="-3000"
                           max="2100">
                </div>

                <div class="form-group col-3">
                    <label for="numero_paginas" class="form-label">Número de páginas</label>
                    <input type="number"
                           id="numero_paginas"
                           name="numero_paginas"
                           class="form-input"
                           placeholder="Ex.: 240"
                           value="<c:out value="${livro.numeroPaginas}"/>"
                           min="1"
                           max="50000">
                </div>
            </div>

            <!-- Linha 3: Sinopse -->
            <div class="form-row">
                <div class="form-group">
                    <label for="sinopse" class="form-label">Sinopse</label>
                    <textarea id="sinopse"
                              name="sinopse"
                              class="form-textarea"
                              placeholder="Escreva uma breve descrição do livro (opcional)"><c:out value="${livro.sinopse}"/></textarea>
                </div>
            </div>

            <!-- Rodapé do Formulário -->
            <div class="form-footer">
                <span class="form-notice">* Campos obrigatórios</span>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/livros" class="btn-white">Cancelar</a>

                    <button type="submit" class="btn-dark">
                        <svg viewBox="0 0 24 24">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="12" y1="8" x2="12" y2="16"></line>
                            <line x1="8" y1="12" x2="16" y2="12"></line>
                        </svg>
                        Adicionar livro
                    </button>
                </div>
            </div>

        </form>

    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
