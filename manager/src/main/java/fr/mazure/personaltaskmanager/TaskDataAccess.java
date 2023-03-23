package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskDataAccess {
    
    private Map<UUID, TaskDatabaseRecord> records;
    
    public TaskDataAccess() {
        this.records = new HashMap<>();
    }

    public void create(final TaskDatabaseDto data) throws ExistingRecordException {
        final TaskDatabaseRecord record = this.records.get(data.uuid());
        if (record != null) throw new ExistingRecordException("Record already exists (UUID=" + data.uuid() + ")");
        this.records.put(data.uuid(), convertDtoToRecord(data));
    }

    public void update(final TaskDatabaseDto data) throws UnexistingRecordException {
        final TaskDatabaseRecord record = this.records.get(data.uuid());
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + data.uuid() + ")");
        this.records.put(data.uuid(), convertDtoToRecord(data));
    }

    public TaskDatabaseDto read(final UUID uuid) throws UnexistingRecordException {
        final TaskDatabaseRecord record = this.records.get(uuid);
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + uuid + ")");
        return convertRecordToDto(record);
    }

    public void delete(final UUID uuid) throws UnexistingRecordException {
        final TaskDatabaseRecord record = this.records.get(uuid);
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + uuid + ")");
        this.records.remove(uuid);
    }

    public List<TaskDatabaseDto> getTasks(final Predicate<TaskDatabaseDto> filter,
                                          final Comparator<TaskDatabaseDto> sorter) {
        return this.records
                   .values()
                   .stream()
                   .map(TaskDataAccess::convertRecordToDto)
                   .filter(filter)
                   .sorted(sorter)
                   .collect(Collectors.toList());
    }

    private static TaskDatabaseRecord convertDtoToRecord(final TaskDatabaseDto data) {
        return new TaskDatabaseRecord(data.uuid(), data.clientTimeStamp(), ZonedDateTime.now(), data.humanId(), data.humanDescription());
    }

    private static TaskDatabaseDto convertRecordToDto(final TaskDatabaseRecord record) {
        return new TaskDatabaseDto(record.uuid(), record.clientTimeStamp(), record.humanId(), record.humanDescription());
    }
}
