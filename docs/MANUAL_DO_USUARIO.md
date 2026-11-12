# Littera - Manual do Usuário Simplificado

**Disciplina:** Projeto Integrador Transdisciplinar em Ciência da Computação  
**Autor:** Victor Michel Fernandes Leite | **RGM:** 47117877  
**Curso:** Bacharelado em Ciência da Computação  
**Data:** Novembro de 2026  

---

## 1. Visão Geral do Sistema

O **Littera** é uma aplicação web moderna, intuitiva e responsiva projetada para a catalogação, pesquisa e gerenciamento da sua coleção pessoal de livros. Com ele, você pode manter um registro completo de suas obras literárias, organizadas por gêneros, com controle de ano de publicação, quantidade de páginas e sinopse.

A aplicação foi construída com foco em simplicidade, segurança e alta fidelidade visual, proporcionando uma experiência de uso fluida e agradável tanto em computadores quanto em dispositivos móveis.

---

## 2. Pré-requisitos do Sistema

Para executar o Littera em seu computador local, você precisará apenas de:

- **Sistema Operacional:** Windows 10/11, Linux ou macOS;
- **Java Runtime Environment (JRE) ou JDK:** Versão 17, 21 ou 22 (recomendado JDK 22);
- **Navegador Web:** Google Chrome, Mozilla Firefox, Microsoft Edge ou Safari atualizados;
- **Porta de Rede:** Porta `8080` livre para comunicação HTTP local.

> **Nota:** Não é necessário instalar nenhum banco de dados externo ou servidor previamente configurado. O Littera já vem com o banco relacional **SQLite embutido** e com o servidor web **Apache Tomcat 10 embutido**, funcionando imediatamente no padrão *zero-configuration*.

---

## 3. Como Executar a Aplicação

### Opção 1: Execução Rápida Embutida (Recomendada)

1. Abra a pasta do projeto `Littera App`;
2. No Windows, dê um duplo clique no arquivo **`run.bat`** (ou abra o terminal PowerShell e digite `.\run.ps1`);
3. O script verificará a compilação, executará os testes unitários e inicializará o servidor Tomcat embutido;
4. Quando a mensagem `[OK] Servidor Web Apache Tomcat 10 Embutido Inicializado!` aparecer no terminal, abra seu navegador de internet e acesse:
   ```
   http://localhost:8080/livros
   ```
5. Para encerrar o servidor a qualquer momento, basta pressionar `Ctrl + C` na janela do terminal.

### Opção 2: Deploy no Apache Tomcat Externo (Arquivo WAR)

Caso deseje implantar o Littera em uma instância existente do Apache Tomcat 10:

1. Execute o script `build.bat` ou `.\build.ps1` para gerar o pacote;
2. Copie o arquivo gerado em `target\littera.war`;
3. Cole o arquivo na pasta `webapps` do seu Apache Tomcat 10 (exemplo: `C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps`);
4. Inicie o serviço do Tomcat;
5. Acesse no navegador:
   ```
   http://localhost:8080/littera/livros
   ```

---

## 4. Configuração do Banco de Dados (Opcional)

Por padrão, a aplicação armazena seus dados no arquivo local `littera.db` (SQLite). 

Se você preferir utilizar um servidor **MySQL**, basta editar o arquivo de configuração localizado em `src/main/resources/db.properties` (ou `target/classes/db.properties`):

```properties
# Altere de 'sqlite' para 'mysql'
db.type=mysql

# Credenciais do seu servidor MySQL local
mysql.url=jdbc:mysql://localhost:3306/littera_db?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
mysql.user=root
mysql.password=sua_senha_aqui
```

Antes de iniciar no MySQL, execute o script SQL disponibilizado em `docs/MODELO_BANCO_DADOS.sql` no MySQL Workbench para criar a base e as tabelas.

---

## 5. Guia de Funcionalidades do Littera

### 5.1. Barra Lateral de Navegação
No canto esquerdo da tela encontra-se a barra lateral fixa com o logotipo característico **L. LITTERA** e o ícone de atalho para a estante da coleção. Clicar em qualquer um desses elementos redireciona você instantaneamente para a página inicial da sua coleção.

### 5.2. Visualização da Coleção
Na tela principal (**Coleção**), todos os livros cadastrados são exibidos em formato de cartões (*cards*). Cada cartão exibe:
- O **Título do livro** em destaque;
- O **Nome do autor** logo abaixo.

Ao posicionar o cursor sobre qualquer cartão, você notará uma leve elevação visual. Clicar sobre o cartão abrirá a tela completa de detalhes daquele livro.

### 5.3. Busca Rápida por Título ou Autor
No topo da página principal encontra-se a barra de pesquisa com o texto de ajuda: *"Faça sua busca por autor ou título"*.
1. Digite uma palavra ou nome (exemplo: `Hercules`, `Saramago`, `Platão`);
2. Pressione **Enter**;
3. O catálogo filtrará instantaneamente os livros correspondentes sem distinção entre maiúsculas e minúsculas;
4. Para limpar a busca, apague o texto e pressione **Enter** novamente.

### 5.4. Filtragem por Gêneros Literários
Abaixo da barra de busca está a seção **Gêneros**, apresentando pílulas interativas com as categorias disponíveis (*Todos, Romance, Ficção, Terror, Filosofia, Ação, Drama, etc.*):
- A pílula verde limão indica o filtro que está ativo no momento;
- Clicar em uma pílula (ex: *Filosofia*) exibirá apenas as obras pertencentes àquele gênero;
- Clicar em *Todos* restaura a exibição de toda a sua biblioteca.

### 5.5. Cadastrando um Novo Livro
1. Na tela principal, clique no botão escuro **"+ Adicionar livro"** no canto superior direito;
2. Na tela de formulário, preencha os dados:
   - **Título do livro \*** *(Obrigatório)*;
   - **Autor \*** *(Obrigatório)*;
   - **Gênero \*** *(Obrigatório – selecione na lista suspensa)*;
   - **Ano de publicação** *(Opcional – exemplo: 2024)*;
   - **Número de páginas** *(Opcional – exemplo: 240)*;
   - **Sinopse** *(Opcional – escreva um resumo ou descrição da obra)*;
3. Clique em **"Adicionar livro"** para gravar no banco;
4. O sistema efetuará as validações de segurança e redirecionará você para a tela de detalhes do livro recém-adicionado com uma mensagem de confirmação.

### 5.6. Consultando os Detalhes da Obra
Ao clicar em qualquer livro da coleção:
- O card principal apresenta o título em fonte expandida, o autor e a etiqueta destacada em verde **"Na coleção"**;
- Três cartões informativos revelam o **Gênero**, o **Ano de publicação** e o **Número de páginas** (caso não informados no cadastro, constará como *"Não informado"*);
- A seção **Sinopse** exibe a resenha completa da obra ou uma orientação para edição caso o campo esteja vazio;
- No topo, o botão **"← Voltar à coleção"** retorna para a lista geral.

### 5.7. Editando um Livro Existente
1. Na página de detalhes do livro, clique no botão superior **"Editar livro"**;
2. O formulário será exibido com todos os campos preenchidos previamente com os valores atuais;
3. Modifique as informações desejadas;
4. Clique em **"Salvar alterações"** no rodapé para atualizar os dados no banco de dados;
5. Se mudar de ideia, clique em **"Cancelar"** ou **"← Voltar aos detalhes"**.

### 5.8. Excluindo um Livro do Catálogo
Para manter a segurança dos seus dados e evitar perdas acidentais:
1. Acesse a tela de **Detalhes do livro** ou a tela de **Edição**;
2. Clique no botão branco **"Excluir livro"** localizado no canto superior direito;
3. Uma janela modal de confirmação será exibida com a pergunta: *"Tem certeza de que deseja excluir o livro [Título] da sua coleção? Esta ação não poderá ser desfeita."*;
4. Clique em **"Confirmar exclusão"** para remover definitivamente ou em **"Cancelar"** para desistir;
5. Ao confirmar, o livro é excluído do banco de dados e você retorna à lista principal com um aviso de sucesso.

---

## 6. Dúvidas Frequentes (FAQ)

- **A acentuação das palavras em português está funcionando?**  
  Sim! Toda a aplicação (Servlets, JSPs, Banco SQLite e MySQL) foi configurada para utilizar a codificação **UTF-8**, permitindo o uso irrestrito de acentos, cedilhas e caracteres especiais.

- **Posso cadastrar novos gêneros literários?**  
  Sim. Os gêneros são gerenciados na tabela `generos` do banco de dados e podem ser expandidos através de novos registros ou via SQL no arquivo de carga inicial.

- **O que acontece se a porta 8080 estiver ocupada por outro programa?**  
  Você pode definir uma porta alternativa criando uma variável de ambiente `PORT` (por exemplo, `PORT=8090 .\run.ps1`).

---

*Littera - Desenvolvido para facilitar o amor pela leitura e o controle literário.*
