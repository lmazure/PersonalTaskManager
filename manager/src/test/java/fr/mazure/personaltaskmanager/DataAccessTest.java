package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
        final UnexistingRecordException thrown = Assertions.assertThrows(UnexistingRecordException.class, () -> {
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
        final UnexistingRecordException thrown = Assertions.assertThrows(UnexistingRecordException.class, () -> {
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
        final String id = "ID1";
        final String description = "description1";
        final TaskDatabaseDto data = new TaskDatabaseDto(uuid, timestamp, id, description);

        final ZonedDateTime timestamp2 = ZonedDateTime.now();
        final String id2 = "ID2";
        final String description2 = "description2";
        final TaskDatabaseDto data2 = new TaskDatabaseDto(uuid, timestamp2, id2, description2);

        final ExistingRecordException thrown = Assertions.assertThrows(ExistingRecordException.class, () -> {
            access.create(data);
            access.create(data2);
        });

        Assertions.assertTrue(thrown.getMessage().contains("Record already exists"));
        Assertions.assertTrue(thrown.getMessage().contains(uuid.toString()));
    }

    @Test
    void testGetList() throws ExistingRecordException {
        final TaskDataAccess access = new TaskDataAccess();

        final UUID uuid1 = UUID.randomUUID();
        final ZonedDateTime timestamp = ZonedDateTime.now();
        final String id = "ID1";
        final String description = "descriptionB";
        final TaskDatabaseDto dataIn1 = new TaskDatabaseDto(uuid1, timestamp, id, description);

        final UUID uuid2 = UUID.randomUUID();
        final ZonedDateTime timestamp2 = ZonedDateTime.now();
        final String id2 = "ID2";
        final String description2 = "descriptionC";
        final TaskDatabaseDto dataIn2 = new TaskDatabaseDto(uuid2, timestamp2, id2, description2);

        final UUID uuid3 = UUID.randomUUID();
        final ZonedDateTime timestamp3 = ZonedDateTime.now();
        final String id3 = "OD3";
        final String description3 = "descriptionD";
        final TaskDatabaseDto dataIn3 = new TaskDatabaseDto(uuid3, timestamp3, id3, description3);

        final UUID uuid4 = UUID.randomUUID();
        final ZonedDateTime timestamp4 = ZonedDateTime.now();
        final String id4 = "ID2";
        final String description4 = "descriptionA";
        final TaskDatabaseDto dataIn4 = new TaskDatabaseDto(uuid4, timestamp4, id4, description4);
        
        access.create(dataIn1);
        access.create(dataIn2);
        access.create(dataIn3);
        access.create(dataIn4);

        final List<TaskDatabaseDto> dataOut = access.getTasks(t -> t.humanId().startsWith("ID"), (t1, t2) -> t1.humanDescription().compareTo(t2.humanDescription()));

        Assertions.assertEquals(3, dataOut.size());
        Assertions.assertEquals("descriptionA", dataOut.get(0).humanDescription());
        Assertions.assertEquals("descriptionB", dataOut.get(1).humanDescription());
        Assertions.assertEquals("descriptionC", dataOut.get(2).humanDescription());
    }
}
