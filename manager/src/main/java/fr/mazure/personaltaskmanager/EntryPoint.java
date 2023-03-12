package fr.mazure.personaltaskmanager;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
 
@Path("/entry-point")
public class EntryPoint {
 
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test salut à toi et aux autres";
    }
}
