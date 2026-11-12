<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Littera - Detalhes do Livro</title>
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

        <!-- Linha Superior de Navegação e Ações -->
        <div class="top-navigation-row">
            <a href="${pageContext.request.contextPath}/livros" class="back-link">
                <svg viewBox="0 0 24 24">
                    <line x1="19" y1="12" x2="5" y2="12"></line>
                    <polyline points="12 19 5 12 12 5"></polyline>
                </svg>
                Voltar à coleção
            </a>

            <div class="top-actions">
                <button type="button" class="btn-white" id="btn-abrir-excluir">
                    Excluir livro
                </button>

                <a href="${pageContext.request.contextPath}/livros/editar?id=${livro.id}" class="btn-dark">
                    Editar livro
                </a>
            </div>
        </div>

        <h1 class="page-title">Detalhes do livro</h1>
        <p class="page-subtitle">Consulte as informações do livro na sua coleção.</p>

        <!-- Feedback de Notificação -->
        <c:if test="${not empty sessionScope.mensagemSucesso}">
            <div class="alert-feedback alert-sucesso">
                <span><c:out value="${sessionScope.mensagemSucesso}"/></span>
            </div>
            <c:remove var="mensagemSucesso" scope="session"/>
        </c:if>

        <!-- Card Principal com Título, Autor e Selo 'Na coleção' -->
        <div class="details-main-card">
            <span class="badge-collection">Na coleção</span>

            <div class="details-label">Título do livro</div>
            <h2 class="details-title"><c:out value="${livro.titulo}"/></h2>
            <div class="details-author"><c:out value="${livro.autor}"/></div>
        </div>

        <!-- Grade com 3 Cards de Informações Complementares -->
        <div class="details-info-grid">
            <div class="info-card">
                <div class="info-card-label">Gênero</div>
                <div class="info-card-value <c:if test="${not empty livro.generoNome}">filled</c:if>">
                    <c:out value="${livro.generoNomeFormatado}"/>
                </div>
            </div>

            <div class="info-card">
                <div class="info-card-label">Ano de publicação</div>
                <div class="info-card-value <c:if test="${not empty livro.anoPublicacao}">filled</c:if>">
                    <c:out value="${livro.anoPublicacaoFormatado}"/>
                </div>
            </div>

            <div class="info-card">
                <div class="info-card-label">Número de páginas</div>
                <div class="info-card-value <c:if test="${not empty livro.numeroPaginas}">filled</c:if>">
                    <c:out value="${livro.numeroPaginasFormatado}"/>
                </div>
            </div>
        </div>

        <!-- Seção de Sinopse -->
        <section class="synopsis-section" aria-labelledby="sinopse-title">
            <h3 id="sinopse-title" class="section-title">Sinopse</h3>

            <div class="synopsis-box">
                <c:choose>
                    <c:when test="${not empty livro.sinopse and not empty livro.sinopse.trim()}">
                        <p class="synopsis-content"><c:out value="${livro.sinopse}"/></p>
                    </c:when>
                    <c:otherwise>
                        <div class="synopsis-empty-title">Nenhuma sinopse adicionada.</div>
                        <div class="synopsis-empty-subtitle">Edite o livro para incluir uma breve descrição da obra.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

    </main>

    <!-- Modal de Confirmação de Exclusão -->
    <div id="modal-excluir" class="modal-overlay">
        <div class="modal-box">
            <h4 class="modal-title">Confirmar exclusão</h4>
            <p class="modal-text">
                Tem certeza de que deseja excluir o livro "<strong><c:out value="${livro.titulo}"/></strong>" da sua coleção? Esta ação não poderá ser desfeita.
            </p>
            <div class="modal-actions">
                <button type="button" class="btn-white" id="btn-cancelar-modal" style="height: 48px; padding: 0 20px;">
                    Cancelar
                </button>
                <form action="${pageContext.request.contextPath}/livros/excluir" method="POST" style="display: inline;">
                    <input type="hidden" name="id" value="${livro.id}">
                    <button type="submit" class="btn-danger">
                        Confirmar exclusão
                    </button>
                </form>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
