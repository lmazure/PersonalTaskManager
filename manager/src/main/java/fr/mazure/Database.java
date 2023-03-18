package fr.mazure;

import java.time.ZonedDateTime;
import java.util.UUID;

import fr.mazure.personaltaskmanager.ExistingRecordException;
import fr.mazure.personaltaskmanager.TaskDataAccess;
import fr.mazure.personaltaskmanager.TaskDatabaseDto;

public class Database {

    static TaskDataAccess access;

    public static void initialize() {
        if (access != null) {
            throw new IllegalStateException("Database has already been initialized");
        }

        access = new TaskDataAccess();
        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn = new TaskDatabaseDto(uuid, timestamp, id, description);
        try {
            access.create(dataIn);
        } catch (final ExistingRecordException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(uuid);
    }

    public static TaskDataAccess get() {
        return access;
    }
}
