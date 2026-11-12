<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Redireciona a raiz para o controlador de livros
    response.sendRedirect(request.getContextPath() + "/livros");
%>
