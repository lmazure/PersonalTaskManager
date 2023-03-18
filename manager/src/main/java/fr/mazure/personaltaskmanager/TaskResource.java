package fr.mazure.personaltaskmanager;

import jakarta.ws.rs.core.Response;

import java.time.ZonedDateTime;
import java.util.UUID;

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
    
    static TaskDataAccess access = new TaskDataAccess();
    static {
        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn = new TaskDatabaseDto(uuid, timestamp, id, description);
        try {
            access.create(dataIn);
        } catch (ExistingRecordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(uuid);
    }
 
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
    public TaskClientDto getTask(@PathParam("taskId") UUID taskId) {
        // code to retrieve task with specified ID from database
        TaskClientDto task;
        try {
            task = convertDatabaseDTOToClientDTO(access.read(taskId));
        } catch (UnexistingRecordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            task = null;
        }
        
        return task;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(TaskClientDto task) {
        try {
            access.create(convertClientDTOToDatabaseDTO(task));
        } catch (ExistingRecordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return Response.status(201).entity("Task created successfully").build();
    }
    
    @PUT
    @Path("/{taskId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("taskId") UUID taskId, TaskClientDto task) {
        // TODO ensure that taskId == task.uuid
        try {
            access.update(convertClientDTOToDatabaseDTO(task));
        } catch (UnexistingRecordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return Response.status(200).entity("Task updated successfully").build();
    }
    
    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") UUID taskId) {
        // TODO ensure that taskId == task.uuid
        try {
            access.delete(taskId);
        } catch (UnexistingRecordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return Response.status(200).entity("Task deleted successfully").build();
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
