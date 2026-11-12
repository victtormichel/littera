package com.littera.dao;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fábrica de Conexões JDBC para o sistema Littera.
 * Implementa o padrão Factory para gerenciar o ciclo de vida e a obtenção de
 * conexões com o banco de dados (SQLite ou MySQL).
 *
 * Realiza também a inicialização automática do esquema e dados essenciais
 * caso a base de dados ainda não esteja estruturada.
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class FabricaDeConexoes {

    private static final Logger LOGGER = Logger.getLogger(FabricaDeConexoes.class.getName());

    private static Properties properties = new Properties();
    private static boolean bancoInicializado = false;
    private static String dbType = "sqlite";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    static {
        carregarConfiguracoes();
    }

    /**
     * Carrega as configurações de db.properties presentes no classpath.
     */
    private static synchronized void carregarConfiguracoes() {
        try (InputStream input = FabricaDeConexoes.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
                dbType = properties.getProperty("db.type", "sqlite").trim().toLowerCase();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Não foi possível carregar db.properties, usando SQLite padrão.", e);
        }

        if ("mysql".equals(dbType)) {
            try {
                String driverClass = properties.getProperty("mysql.driver", "com.mysql.cj.jdbc.Driver");
                Class.forName(driverClass);
                dbUrl = properties.getProperty("mysql.url");
                dbUser = properties.getProperty("mysql.user", "root");
                dbPassword = properties.getProperty("mysql.password", "");
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Driver MySQL não encontrado no classpath, revertendo para SQLite.", e);
                dbType = "sqlite";
            }
        }

        if ("sqlite".equals(dbType)) {
            try {
                Class.forName("org.sqlite.JDBC");
                String urlProp = properties.getProperty("sqlite.url", "jdbc:sqlite:littera.db");
                // Garante que o arquivo do banco fique na pasta do projeto
                if (!urlProp.contains("/") && !urlProp.contains("\\")) {
                    dbUrl = urlProp;
                } else {
                    dbUrl = urlProp;
                }
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Driver SQLite JDBC não encontrado no classpath.", e);
            }
        }
    }

    /**
     * Obtém uma conexão ativa com o banco de dados configurado.
     *
     * @return Objeto Connection aberto
     * @throws SQLException Em caso de falha na comunicação com o SGBD
     */
    public static Connection getConexao() throws SQLException {
        Connection conn;
        if ("mysql".equals(dbType)) {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } else {
            conn = DriverManager.getConnection(dbUrl);
            // Habilita suporte a chaves estrangeiras no SQLite
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        }

        if (!bancoInicializado) {
            inicializarBancoSeNecessario(conn);
        }

        return conn;
    }

    /**
     * Cria as tabelas e popula dados iniciais caso ainda não existam.
     *
     * @param conn Conexão aberta com o banco
     */
    private static synchronized void inicializarBancoSeNecessario(Connection conn) {
        if (bancoInicializado) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            LOGGER.info("Verificando estrutura do banco de dados (" + dbType + ")...");

            if ("sqlite".equals(dbType)) {
                // Tabela generos
                stmt.execute("CREATE TABLE IF NOT EXISTS generos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nome TEXT NOT NULL UNIQUE" +
                        ");");

                // Tabela livros
                stmt.execute("CREATE TABLE IF NOT EXISTS livros (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "titulo TEXT NOT NULL, " +
                        "autor TEXT NOT NULL, " +
                        "ano_publicacao INTEGER, " +
                        "numero_paginas INTEGER, " +
                        "sinopse TEXT, " +
                        "genero_id INTEGER, " +
                        "FOREIGN KEY (genero_id) REFERENCES generos(id) ON DELETE SET NULL" +
                        ");");

                // Índices
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_livros_titulo ON livros(titulo);");
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_livros_autor ON livros(autor);");
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_livros_genero ON livros(genero_id);");

                // Verifica se há gêneros cadastrados
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM generos")) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        LOGGER.info("Populando gêneros iniciais no banco SQLite...");
                        stmt.execute("INSERT INTO generos (id, nome) VALUES " +
                                "(1, 'Romance'), (2, 'Ficção'), (3, 'Terror'), " +
                                "(4, 'Filosofia'), (5, 'Ação'), (6, 'Drama'), " +
                                "(7, 'Aventura'), (8, 'Biografia'), (9, 'Poesia'), " +
                                "(10, 'Fantasia'), (11, 'História');");

                        // Insere livro de demonstração fiel às referências visuais
                        stmt.execute("INSERT INTO livros (titulo, autor, ano_publicacao, numero_paginas, sinopse, genero_id) VALUES " +
                                "('Hora de Crescer Interiormente - O mito de Hercules hoje', 'Trigueirinho', 2024, 240, 'Uma reflexão profunda sobre o desenvolvimento interior da consciência e os desafios arquetípicos representados pelos doze trabalhos de Hércules aplicados à vida moderna.', 4);");
                    }
                }
            } else if ("mysql".equals(dbType)) {
                stmt.execute("CREATE TABLE IF NOT EXISTS generos (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "nome VARCHAR(100) NOT NULL UNIQUE" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

                stmt.execute("CREATE TABLE IF NOT EXISTS livros (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "titulo VARCHAR(255) NOT NULL, " +
                        "autor VARCHAR(150) NOT NULL, " +
                        "ano_publicacao INT, " +
                        "numero_paginas INT, " +
                        "sinopse TEXT, " +
                        "genero_id INT, " +
                        "CONSTRAINT fk_livros_genero FOREIGN KEY (genero_id) REFERENCES generos(id) ON DELETE SET NULL" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM generos")) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        LOGGER.info("Populando gêneros iniciais no banco MySQL...");
                        stmt.execute("INSERT INTO generos (id, nome) VALUES " +
                                "(1, 'Romance'), (2, 'Ficção'), (3, 'Terror'), " +
                                "(4, 'Filosofia'), (5, 'Ação'), (6, 'Drama'), " +
                                "(7, 'Aventura'), (8, 'Biografia'), (9, 'Poesia'), " +
                                "(10, 'Fantasia'), (11, 'História');");

                        stmt.execute("INSERT INTO livros (titulo, autor, ano_publicacao, numero_paginas, sinopse, genero_id) VALUES " +
                                "('Hora de Crescer Interiormente - O mito de Hercules hoje', 'Trigueirinho', 2024, 240, 'Uma reflexão profunda sobre o desenvolvimento interior da consciência e os desafios arquetípicos representados pelos doze trabalhos de Hércules aplicados à vida moderna.', 4);");
                    }
                }
            }

            bancoInicializado = true;
            LOGGER.info("Banco de dados (" + dbType + ") inicializado com sucesso.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro durante a inicialização automática do banco de dados.", e);
        }
    }

    /**
     * Retorna o tipo de banco em uso.
     *
     * @return "sqlite" ou "mysql"
     */
    public static String getTipoBanco() {
        return dbType;
    }
}
