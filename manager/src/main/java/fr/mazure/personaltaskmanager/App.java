package fr.mazure.personaltaskmanager;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import fr.mazure.Database;
 
public class App {
    public static void main(String[] args) throws Exception {

        Database.initialize();
        
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
 
        final Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);
 
        final ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
 
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
           "jersey.config.server.provider.classnames",
           TaskResource.class.getCanonicalName());
 
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }
}
