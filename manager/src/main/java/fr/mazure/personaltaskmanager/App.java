package fr.mazure.personaltaskmanager;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class App {
    public static final String BASE_URI = "http://localhost:8080/";

    public static Server startServer() {

        // scan packages
        final ResourceConfig config = new ResourceConfig().packages("fr.mazure.personaltaskmanager");

        //final ResourceConfig config = new ResourceConfig(TaskResource.class);
        final Server server =
                JettyHttpContainerFactory.createServer(URI.create(BASE_URI), config);

        return server;

    }

    public static void main(String[] args) throws Exception {

        Database.initialize();
                final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn = new TaskDatabaseDto(uuid, timestamp, id, description);

        Database.get().create(dataIn);
        try {

            final Server server = startServer();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the application...");
                    server.stop();
                    System.out.println("Done, exit.");
                } catch (Exception e) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
                }
            }));

            System.out.println(String.format("Application started.%nStop the application using CTRL+C"));

            // block and wait shut down signal, like CTRL+C
            Thread.currentThread().join();

            // alternative
            // Thread.sleep(Long.MAX_VALUE);       // sleep forever...
            // Thread.sleep(Integer.MAX_VALUE);    // sleep around 60+ years

        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}