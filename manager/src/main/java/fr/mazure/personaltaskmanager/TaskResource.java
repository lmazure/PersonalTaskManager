package fr.mazure.personaltaskmanager;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.time.ZonedDateTime;
import java.util.UUID;

import fr.mazure.Database;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/tasks")
public class TaskResource {
    

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test salut à toi et aux autres";
    }

    // TODO add the method to get all tasks

    @GET
    @Path("/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("taskId") final UUID taskId) {
        // code to retrieve task with specified ID from database
        TaskClientDto task;
        try {
            task = convertDatabaseDTOToClientDTO(Database.get().read(taskId));
        } catch (final UnexistingRecordException e) {
            return Response.status(Status.NOT_FOUND.getStatusCode()).entity("Task not found").build();
        }
        
        return Response.ok(task).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(final TaskClientDto task) {
        try {
            Database.get().create(convertClientDTOToDatabaseDTO(task));
        } catch (final ExistingRecordException e) {
            return Response.status(Status.CONFLICT.getStatusCode()).entity("Task already exists").build();
        }
        
        return Response.status(Status.CREATED.getStatusCode()).entity("Task created successfully").build();
    }
    
    @PUT
    @Path("/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("taskId") final UUID taskId, final TaskClientDto task) {
        // TODO ensure that taskId == task.uuid
        try {
            Database.get().update(convertClientDTOToDatabaseDTO(task));
        } catch (final UnexistingRecordException e) {
            return Response.status(Status.NOT_FOUND.getStatusCode()).entity("Task not found").build();
        }
        
        return Response.status(Status.OK.getStatusCode()).entity("Task updated successfully").build();
    }
    
    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") final UUID taskId) {
        // TODO ensure that taskId == task.uuid
        try {
            Database.get().delete(taskId);
        } catch (final UnexistingRecordException e) {
            return Response.status(Status.NOT_FOUND.getStatusCode()).entity("Task not found").build();
        }
        
        return Response.status(Status.OK.getStatusCode()).entity("Task deleted successfully").build();
    }

    private TaskClientDto convertDatabaseDTOToClientDTO(final TaskDatabaseDto databaseDto) {
        final String clientTimeStamp = databaseDto.clientTimeStamp().toString();
        return new TaskClientDto(databaseDto.uuid(), clientTimeStamp, databaseDto.humanId(), databaseDto.humanDescription());
    }

    private TaskDatabaseDto convertClientDTOToDatabaseDTO(final TaskClientDto clientDto) {
        final ZonedDateTime clientTimeStamp = ZonedDateTime.parse(clientDto.clientTimeStamp());
        return new TaskDatabaseDto(clientDto.uuid(), clientTimeStamp, clientDto.humanId(), clientDto.humanDescription());
    }
}
