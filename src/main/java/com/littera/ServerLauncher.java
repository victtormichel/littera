package com.littera;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.util.logging.Logger;

/**
 * Inicializador Embutido (Embedded Server Launcher) para o Littera.
 * Permite a execução imediata e autônoma da aplicação sem requerer instalação
 * ou permissões de administrador sobre servidores de aplicação externos.
 *
 * Utiliza o Apache Tomcat 10.1 (Jakarta EE 10 / Servlet 6.0) embutido.
 *
 * @author Victor Michel Fernandes Leite (RGM: 47117877)
 * @version 1.0
 */
public class ServerLauncher {

    private static final Logger LOGGER = Logger.getLogger(ServerLauncher.class.getName());

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.trim().isEmpty()) {
            try {
                port = Integer.parseInt(portEnv.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        // Diretório base temporário para o Tomcat Embed
        File baseDir = new File("target/tomcat-embed");
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.setPort(port);
        tomcat.getConnector(); // Inicializa o conector HTTP padrão (NIO)

        // Localização da pasta webapp (JSPs, CSS, JS, WEB-INF)
        File webappDir = new File("src/main/webapp");
        if (!webappDir.exists()) {
            webappDir = new File("webapp");
        }

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappDir.getAbsolutePath());

        // Vincula as classes compiladas ao classpath do contexto WEB-INF/classes
        File classesDir = new File("target/classes");
        if (!classesDir.exists()) {
            classesDir = new File("build/classes");
        }

        if (classesDir.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(
                    resources,
                    "/WEB-INF/classes",
                    classesDir.getAbsolutePath(),
                    "/"
            ));
            ctx.setResources(resources);
        }

        System.out.println("\n" +
                "===================================================================\n" +
                "               LITTERA - CATÁLOGO DE LIVROS                        \n" +
                "===================================================================\n" +
                " [OK] Servidor Web Apache Tomcat 10 Embutido Inicializado!\n" +
                " [OK] Banco de Dados: SQLite / JDBC Integrado\n" +
                " [OK] Acesso Disponível em: http://localhost:" + port + "/livros\n" +
                "===================================================================\n" +
                " Pressione Ctrl+C nesta janela para encerrar o servidor.\n");

        tomcat.start();
        tomcat.getServer().await();
    }
}
