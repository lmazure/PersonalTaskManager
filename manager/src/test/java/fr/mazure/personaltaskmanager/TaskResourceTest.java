package fr.mazure.personaltaskmanager;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.mazure.Database;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests of TaskResource class
 */
public class TaskResourceTest extends JerseyTest {

    @BeforeAll
    public static void commonSetup() {
        Database.initialize();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TaskResource.class);
    }

    @Test
    void testCreate() {
        final Entity<String> jsonCreate = Entity.json("{\"uuid\":\"661d0edc-f1ae-477f-9bce-b4396de50805\",\"clientTimeStamp\":\"2023-03-18T07:25:13.757705700+01:00[Europe/Paris]\",\"humanId\":\"ID\",\"humanDescription\":\"description\"}");
        final Response responseCreate = target("tasks").request()
                                                       .post(jsonCreate);

       Assertions.assertEquals(Status.CREATED.getStatusCode(), responseCreate.getStatus());
       Assertions.assertEquals("Task created successfully", responseCreate.readEntity(String.class));
    }

    @Test
    void testCreateRead() {
        final Entity<String> jsonCreate = Entity.json("{\"uuid\":\"661d0edc-f1ae-477f-9bce-b4396de50805\",\"clientTimeStamp\":\"2023-03-18T07:25:13.757705700+01:00[Europe/Paris]\",\"humanId\":\"ID\",\"humanDescription\":\"description\"}");
        target("tasks").request()
                       .post(jsonCreate);
        final Response responseGet = target("tasks/661d0edc-f1ae-477f-9bce-b4396de50805").request()
                                                                                         .get();
        Assertions.assertEquals(Status.OK.getStatusCode(), responseGet.getStatus());
        Assertions.assertEquals("{\"uuid\":\"661d0edc-f1ae-477f-9bce-b4396de50805\",\"clientTimeStamp\":\"2023-03-18T07:25:13.757705700+01:00[Europe/Paris]\",\"humanId\":\"ID\",\"humanDescription\":\"description\"}", responseGet.readEntity(String.class));
    }


    @Test
    void testCreateDeleteRead() {
        final Entity<String> jsonCreate = Entity.json("{\"uuid\":\"661d0edc-f1ae-477f-9bce-b4396de50805\",\"clientTimeStamp\":\"2023-03-18T07:25:13.757705700+01:00[Europe/Paris]\",\"humanId\":\"ID\",\"humanDescription\":\"description\"}");
        target("tasks").request()
                       .post(jsonCreate);
        target("tasks/661d0edc-f1ae-477f-9bce-b4396de50805").request()
                                                            .delete();
        final Response responseGet = target("tasks/661d0edc-f1ae-477f-9bce-b4396de50805").request()
                                                                                         .get();
        Assertions.assertEquals(Status.NOT_FOUND.getStatusCode(), responseGet.getStatus());
        Assertions.assertEquals("Task not found", responseGet.readEntity(String.class));
    }
}
