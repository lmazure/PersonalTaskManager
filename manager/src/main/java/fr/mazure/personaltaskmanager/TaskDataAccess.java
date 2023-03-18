package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TaskDataAccess {
    
    private Map<UUID, TaskDatabaseRecord> records;
    
    public TaskDataAccess() {
        this.records = new HashMap<>();
    }

    public void create(final TaskDatabaseDto data) throws ExistingRecordException {
        final TaskDatabaseRecord record = this.records.get(data.uuid());
        if (record != null) throw new ExistingRecordException("Record already exists (UUID=" + data.uuid() + ")");
        this.records.put(data.uuid(), convertDataToRecord(data));
    }

    public void update(final TaskDatabaseDto data) throws UnexistingRecordException {
        final TaskDatabaseRecord record = this.records.get(data.uuid());
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + data.uuid() + ")");
        this.records.put(data.uuid(), convertDataToRecord(data));
    }

    public TaskDatabaseDto read(final UUID uuid) throws UnexistingRecordException {
        final TaskDatabaseRecord record = this.records.get(uuid);
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + uuid + ")");
        return convertRecordToDate(record);
    }

    public void delete(final UUID uuid) throws UnexistingRecordException {
        final TaskDatabaseRecord record = this.records.get(uuid);
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + uuid + ")");
        this.records.remove(uuid);
    }

    private TaskDatabaseRecord convertDataToRecord(final TaskDatabaseDto data) {
        return new TaskDatabaseRecord(data.uuid(), data.clientTimeStamp(), ZonedDateTime.now(), data.humanId(), data.humanDescription());
    }

    private TaskDatabaseDto convertRecordToDate(final TaskDatabaseRecord record) {
        return new TaskDatabaseDto(record.uuid(), record.clientTimeStamp(), record.humanId(), record.humanDescription());
    }
}
