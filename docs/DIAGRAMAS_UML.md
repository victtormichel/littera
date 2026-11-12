# Littera - Modelagem Visual e Diagramas UML

**Disciplina:** Projeto Integrador Transdisciplinar em Ciência da Computação  
**Autor:** Victor Michel Fernandes Leite | **RGM:** 47117877  
**Curso:** Bacharelado em Ciência da Computação – Novembro de 2026

Este documento reúne os diagramas da modelagem de software do projeto **Littera - Catálogo Simples de Livros**, elaborado segundo as convenções da **UML (Unified Modeling Language)** e modelagem conceitual de banco de dados (DER).

---

## 1. Diagrama de Casos de Uso (UML)

Representa os atores e suas interações com as funcionalidades do sistema.

```mermaid
flowchart LR
    Usuario(["Usuário / Leitor"])

    subgraph Sistema Littera ["Sistema Littera (Catálogo de Livros)"]
        UC1(["UC01 - Consultar Coleção de Livros"])
        UC2(["UC02 - Buscar Livro por Título/Autor"])
        UC3(["UC03 - Filtrar Livros por Gênero"])
        UC4(["UC04 - Visualizar Detalhes do Livro"])
        UC5(["UC05 - Cadastrar Novo Livro"])
        UC6(["UC06 - Editar Informações do Livro"])
        UC7(["UC07 - Excluir Livro da Coleção"])
        UC8(["UC08 - Validar Campos Obrigatórios"])
    end

    Usuario --> UC1
    Usuario --> UC2
    Usuario --> UC3
    Usuario --> UC4
    Usuario --> UC5
    Usuario --> UC6
    Usuario --> UC7

    UC5 -.->|<<include>>| UC8
    UC6 -.->|<<include>>| UC8
    UC4 -.->|<<extend>>| UC6
    UC4 -.->|<<extend>>| UC7
```

### Especificação Resumida dos Casos de Uso
- **UC01 - Consultar Coleção:** O usuário acessa a página inicial (`/livros`) e visualiza os cards de todos os livros catalogados.
- **UC02 - Buscar Livro:** O usuário informa um termo no campo de pesquisa e o sistema realiza busca SQL `LIKE ?` em título e autor.
- **UC03 - Filtrar por Gênero:** O usuário seleciona uma pílula de gênero (ex: Filosofia, Ficção) e visualiza apenas obras correspondentes.
- **UC04 - Visualizar Detalhes:** O usuário clica no card e é direcionado para a página com sinopse, ano, páginas e gênero.
- **UC05 - Cadastrar Novo Livro:** O usuário preenche o formulário com dados obrigatórios (título, autor, gênero) e opcionais.
- **UC06 - Editar Livro:** O usuário altera atributos de uma obra existente e confirma as alterações.
- **UC07 - Excluir Livro:** O usuário confirma a remoção do livro através de diálogo modal defensivo.
- **UC08 - Validar Campos:** Validação de integridade no servidor (campos não vazios, limites de caracteres e valores numéricos).

---

## 2. Diagrama de Classes (UML)

Representa a estrutura estática do sistema, os modelos de domínio, controladores e objetos de acesso a dados (padrão MVC + DAO).

```mermaid
classDiagram
    class Genero {
        -Integer id
        -String nome
        +Genero()
        +Genero(String nome)
        +Genero(Integer id, String nome)
        +getId() Integer
        +setId(Integer id) void
        +getNome() String
        +setNome(String nome) void
        +equals(Object o) boolean
        +hashCode() int
        +toString() String
    }

    class Livro {
        -Integer id
        -String titulo
        -String autor
        -Integer anoPublicacao
        -Integer numeroPaginas
        -String sinopse
        -Integer generoId
        -String generoNome
        +Livro()
        +Livro(String titulo, String autor)
        +getId() Integer
        +setId(Integer id) void
        +getTitulo() String
        +setTitulo(String titulo) void
        +getAutor() String
        +setAutor(String autor) void
        +getAnoPublicacao() Integer
        +setAnoPublicacao(Integer ano) void
        +getNumeroPaginas() Integer
        +setNumeroPaginas(Integer paginas) void
        +getSinopse() String
        +setSinopse(String sinopse) void
        +getGeneroId() Integer
        +setGeneroId(Integer id) void
        +getGeneroNome() String
        +setGeneroNome(String nome) void
        +getAnoPublicacaoFormatado() String
        +getNumeroPaginasFormatado() String
        +getGeneroNomeFormatado() String
    }

    class FabricaDeConexoes {
        -Properties properties$
        -boolean bancoInicializado$
        -String dbType$
        -String dbUrl$
        +getConexao() Connection$
        -inicializarBancoSeNecessario(Connection conn)$
        +getTipoBanco() String$
    }

    class GeneroDAO {
        -Logger LOGGER
        +listarTodos() List~Genero~
        +buscarPorId(int id) Genero
        +buscarPorNome(String nome) Genero
        +inserir(Genero genero) boolean
        +atualizar(Genero genero) boolean
        +excluir(int id) boolean
    }

    class LivroDAO {
        -Logger LOGGER
        +listarTodos() List~Livro~
        +buscarPorId(int id) Livro
        +buscarPorTermo(String termo) List~Livro~
        +listarPorGenero(int generoId) List~Livro~
        +buscarComFiltro(String termo, Integer generoId) List~Livro~
        +inserir(Livro livro) boolean
        +atualizar(Livro livro) boolean
        +excluir(int id) boolean
        +contarTotal() int
        -mapearLivro(ResultSet rs) Livro
    }

    class LivroServlet {
        -LivroDAO livroDAO
        -GeneroDAO generoDAO
        +init() void
        #doGet(HttpServletRequest req, HttpServletResponse resp) void
        #doPost(HttpServletRequest req, HttpServletResponse resp) void
        -processarRequisicao(HttpServletRequest req, HttpServletResponse resp) void
        -listarLivros(HttpServletRequest req, HttpServletResponse resp) void
        -exibirDetalhes(HttpServletRequest req, HttpServletResponse resp) void
        -exibirFormularioCadastro(HttpServletRequest req, HttpServletResponse resp) void
        -salvarLivro(HttpServletRequest req, HttpServletResponse resp) void
        -exibirFormularioEdicao(HttpServletRequest req, HttpServletResponse resp) void
        -atualizarLivro(HttpServletRequest req, HttpServletResponse resp) void
        -excluirLivro(HttpServletRequest req, HttpServletResponse resp) void
    }

    Livro "1" --> "0..1" Genero : associado a
    LivroDAO ..> Livro : manipula
    LivroDAO ..> FabricaDeConexoes : utiliza
    GeneroDAO ..> Genero : manipula
    GeneroDAO ..> FabricaDeConexoes : utiliza
    LivroServlet --> LivroDAO : delega persistência
    LivroServlet --> GeneroDAO : consulta gêneros
```

---

## 3. Diagrama Entidade-Relacionamento (DER)

Representa o modelo de dados relacional com a cardinalidade entre `generos` e `livros`.

```mermaid
erDiagram
    GENEROS ||--o{ LIVROS : "classifica"

    GENEROS {
        int id PK "Identificador único (Auto Increment)"
        varchar(100) nome "Nome do gênero literário (Único)"
    }

    LIVROS {
        int id PK "Identificador primário"
        varchar(255) titulo "Título da obra literária"
        varchar(150) autor "Nome do autor ou autora"
        int ano_publicacao "Ano de lançamento/edição"
        int numero_paginas "Contagem de páginas"
        text sinopse "Descrição resumida da obra"
        int genero_id FK "Referência à tabela generos"
    }
```

---

## 4. Diagrama de Sequência: Cadastrar Novo Livro

Demonstra a troca de mensagens entre o Navegador, o Servlet Controller, o DAO, a Fábrica de Conexões e a JSP de Visualização.

```mermaid
sequenceDiagram
    autonumber
    actor Usuario as Usuário / Browser
    participant Servlet as LivroServlet
    participant DAO as LivroDAO
    participant Fabrica as FabricaDeConexoes
    participant Banco as SGBD (SQLite/MySQL)
    participant JSP as detalhes-livro.jsp

    Usuario->>Servlet: POST /livros/salvar (titulo, autor, genero_id, ...)
    activate Servlet
    Servlet->>Servlet: Validar campos obrigatórios e tipos
    alt Dados Inválidos
        Servlet-->>Usuario: Forward /adicionar-livro.jsp (com erros)
    else Dados Válidos
        Servlet->>DAO: inserir(novoLivro)
        activate DAO
        DAO->>Fabrica: getConexao()
        activate Fabrica
        Fabrica-->>DAO: Connection
        deactivate Fabrica
        DAO->>Banco: PreparedStatement.executeUpdate(INSERT INTO livros...)
        activate Banco
        Banco-->>DAO: Linhas afetadas e ID gerado
        deactivate Banco
        DAO-->>Servlet: true (ID atribuído ao livro)
        deactivate DAO
        Servlet-->>Usuario: Redirect /livros/detalhes?id={id}
    end
    deactivate Servlet

    Usuario->>Servlet: GET /livros/detalhes?id={id}
    activate Servlet
    Servlet->>DAO: buscarPorId(id)
    activate DAO
    DAO->>Banco: PreparedStatement.executeQuery(SELECT ... LEFT JOIN generos ...)
    activate Banco
    Banco-->>DAO: ResultSet
    deactivate Banco
    DAO-->>Servlet: Objeto Livro populado
    deactivate DAO
    Servlet->>JSP: Forward com request.setAttribute("livro", livro)
    activate JSP
    JSP-->>Usuario: Renderiza HTML com dados e badge 'Na coleção'
    deactivate JSP
    deactivate Servlet
```
