package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataAccessTest {

    @Test
    void testCreateRead() throws ExistingRecordException, UnexistingRecordException {
        final TaskDataAccess access = new TaskDataAccess();

        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn = new TaskDatabaseDto(uuid, timestamp, id, description);

        access.create(dataIn);
        final TaskDatabaseDto dataOut = access.read(uuid);
        
        Assertions.assertEquals(uuid, dataOut.uuid());
        Assertions.assertEquals(timestamp, dataOut.clientTimeStamp());
        Assertions.assertEquals(id, dataOut.humanId());
        Assertions.assertEquals(description, dataOut.humanDescription());
    }

    @Test
    void testCreateUpdateRead() throws ExistingRecordException, UnexistingRecordException {
        final TaskDataAccess access = new TaskDataAccess();

        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now().minus(2, ChronoUnit.MONTHS);
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn1 = new TaskDatabaseDto(uuid, timestamp, id, description);

        final ZonedDateTime timestamp2 = ZonedDateTime.now();
        final String id2 = "ID2";
        final String description2 = "description2";
        final TaskDatabaseDto dataIn2 = new TaskDatabaseDto(uuid, timestamp2, id2, description2);

        access.create(dataIn1);
        access.update(dataIn2);
        final TaskDatabaseDto dataOut = access.read(uuid);
        
        Assertions.assertEquals(uuid, dataOut.uuid());
        Assertions.assertEquals(timestamp2, dataOut.clientTimeStamp());
        Assertions.assertEquals(id2, dataOut.humanId());
        Assertions.assertEquals(description2, dataOut.humanDescription());
    }

    @Test
    void testCreateDeleteRead() throws ExistingRecordException, UnexistingRecordException {
        final TaskDataAccess access = new TaskDataAccess();

        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now().minus(2, ChronoUnit.MONTHS);
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn1 = new TaskDatabaseDto(uuid, timestamp, id, description);

        access.create(dataIn1);
        access.delete(uuid);
        UnexistingRecordException thrown = Assertions.assertThrows(UnexistingRecordException.class, () -> {
            access.read(uuid);
        });

        Assertions.assertTrue(thrown.getMessage().contains("Unkwown record"));
        Assertions.assertTrue(thrown.getMessage().contains(uuid.toString()));
    }

    @Test
    void testCreateDeleteDelete() throws ExistingRecordException, UnexistingRecordException {
        final TaskDataAccess access = new TaskDataAccess();

        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now().minus(2, ChronoUnit.MONTHS);
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto dataIn1 = new TaskDatabaseDto(uuid, timestamp, id, description);

        access.create(dataIn1);
        access.delete(uuid);
        UnexistingRecordException thrown = Assertions.assertThrows(UnexistingRecordException.class, () -> {
            access.delete(uuid);
        });

        Assertions.assertTrue(thrown.getMessage().contains("Unkwown record"));
        Assertions.assertTrue(thrown.getMessage().contains(uuid.toString()));
    }

    @Test
    void testCreateAlreadyCreated() throws ExistingRecordException {
        final TaskDataAccess access = new TaskDataAccess();

        final UUID uuid = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final String id = "ID";
        final String description = "description";
        final TaskDatabaseDto data = new TaskDatabaseDto(uuid, timestamp, id, description);

        final ZonedDateTime timestamp2 = ZonedDateTime.now();
        final String id2 = "ID2";
        final String description2 = "description2";
        final TaskDatabaseDto data2 = new TaskDatabaseDto(uuid, timestamp2, id2, description2);

        ExistingRecordException thrown = Assertions.assertThrows(ExistingRecordException.class, () -> {
            access.create(data);
            access.create(data2);
        });

        Assertions.assertTrue(thrown.getMessage().contains("Record already exists"));
        Assertions.assertTrue(thrown.getMessage().contains(uuid.toString()));
    }
}
