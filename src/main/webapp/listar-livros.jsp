<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Littera - Coleção de Livros</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Barra de Navegação Lateral (Sidebar) -->
    <aside class="sidebar">
        <a href="${pageContext.request.contextPath}/livros" class="brand-container" title="Littera Home">
            <span class="brand-logo-letter">L.</span>
            <span class="brand-name">LITTERA</span>
        </a>

        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/livros" class="nav-item" title="Coleção de Livros">
                <!-- Ícone de Livros na Estante -->
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

        <!-- Notificações de Sucesso ou Erro (Flash Messages) -->
        <c:if test="${not empty sessionScope.mensagemSucesso}">
            <div class="alert-feedback alert-sucesso">
                <span><c:out value="${sessionScope.mensagemSucesso}"/></span>
            </div>
            <c:remove var="mensagemSucesso" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.mensagemErro}">
            <div class="alert-feedback alert-erro">
                <span><c:out value="${sessionScope.mensagemErro}"/></span>
            </div>
            <c:remove var="mensagemErro" scope="session"/>
        </c:if>

        <!-- Cabeçalho da Coleção: Campo de Pesquisa + Botão Adicionar -->
        <header class="collection-header">
            <form action="${pageContext.request.contextPath}/livros" method="GET" class="search-box-form">
                <svg class="search-icon" viewBox="0 0 24 24">
                    <circle cx="11" cy="11" r="8"></circle>
                    <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                </svg>
                <input type="text"
                       name="q"
                       class="search-input"
                       placeholder="Faça sua busca por autor ou título"
                       value="<c:out value="${termoBusca}"/>"
                       autocomplete="off">
                <c:if test="${not empty generoSelecionado}">
                    <input type="hidden" name="genero" value="<c:out value="${generoSelecionado}"/>">
                </c:if>
            </form>

            <a href="${pageContext.request.contextPath}/livros/novo" class="btn-dark" title="Cadastrar novo livro">
                <!-- Ícone Plus Circle -->
                <svg viewBox="0 0 24 24">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="16"></line>
                    <line x1="8" y1="12" x2="16" y2="12"></line>
                </svg>
                Adicionar livro
            </a>
        </header>

        <!-- Seção de Filtros por Gênero -->
        <section aria-labelledby="generos-title">
            <h2 id="generos-title" class="section-title">Gêneros</h2>
            <div class="genres-container">
                <!-- Pílula 'Todos' -->
                <a href="${pageContext.request.contextPath}/livros<c:if test="${not empty termoBusca}">?q=<c:out value="${termoBusca}"/></c:if>"
                   class="genre-pill <c:if test="${empty generoSelecionado}">active</c:if>">
                    Todos
                </a>

                <!-- Pílulas Dinâmicas carregadas do Banco -->
                <c:forEach var="gen" items="${generos}">
                    <a href="${pageContext.request.contextPath}/livros?genero=${gen.id}<c:if test="${not empty termoBusca}">&q=<c:out value="${termoBusca}"/></c:if>"
                       class="genre-pill <c:if test="${generoSelecionado == gen.id}">active</c:if>">
                        <c:out value="${gen.nome}"/>
                    </a>
                </c:forEach>
            </div>
        </section>

        <!-- Seção da Coleção (Cards de Livros) -->
        <section aria-labelledby="colecao-title">
            <h2 id="colecao-title" class="section-title">Coleção</h2>

            <div class="books-grid">
                <c:choose>
                    <c:when test="${not empty livros}">
                        <c:forEach var="livro" items="${livros}">
                            <a href="${pageContext.request.contextPath}/livros/detalhes?id=${livro.id}" class="book-card" title="Ver detalhes de ${livro.titulo}">
                                <div class="book-card-title"><c:out value="${livro.titulo}"/></div>
                                <div class="book-card-author"><c:out value="${livro.autor}"/></div>
                            </a>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>Nenhum livro encontrado</h3>
                            <p>
                                <c:choose>
                                    <c:when test="${not empty termoBusca}">
                                        Não encontramos livros correspondentes ao termo "<strong><c:out value="${termoBusca}"/></strong>".
                                    </c:when>
                                    <c:otherwise>
                                        Sua coleção ainda está vazia ou o gênero filtrado não possui títulos cadastrados.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
