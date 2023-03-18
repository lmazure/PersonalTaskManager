package fr.mazure.personaltaskmanager;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.Database;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class TaskResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        Database.initialize();
        return new ResourceConfig(TaskResource.class);
    }

    @Test
    void testCreate() {
        final Response response = target("tasks").request()
                                                 .post(Entity.json("{\"uuid\":\"661d0edc-f1ae-477f-9bce-b4396de50805\",\"clientTimeStamp\":\"2023-03-18T07:25:13.757705700+01:00[Europe/Paris]\",\"humanId\":\"ID\",\"humanDescription\":\"description\"}"));

    Assertions.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    Assertions.assertTrue(response.readEntity(String.class).equals("Task created successfully"));
    }

}
