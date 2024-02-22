package fr.mazure.personaltaskmanager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;



public class App {
    public static final String BASE_URI = "http://localhost:8080/";

    public static Server startServer() {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages","fr.mazure.personaltaskmanager");
        ServletHolder staticServlet = context.addServlet(DefaultServlet.class,"/*");
        staticServlet.setInitParameter("resourceBase","src/main/webapp");
        staticServlet.setInitParameter("pathInfoOnly","true");
        try {
            server.start();
        } catch (final Exception e) {
            System.err.println("Failed to start server: " + e);
            System.exit(1);
        }
        return server;
    }

    public static void main(String[] args) throws Exception {

        Database.initialize();

        try {

            final Server server = startServer();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the application...");
                    server.stop();
                    System.out.println("Done, exit.");
                } catch (final Exception e) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
                }
            }));

            System.out.println(String.format("Application started.%nStop the application using CTRL+C"));

            // block and wait shut down signal, like CTRL+C
            Thread.currentThread().join();

            // alternative
            // Thread.sleep(Long.MAX_VALUE);       // sleep forever...
            // Thread.sleep(Integer.MAX_VALUE);    // sleep around 60+ years

        } catch (final InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}