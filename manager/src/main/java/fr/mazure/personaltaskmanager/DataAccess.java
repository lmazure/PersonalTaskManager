package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataAccess {
    
    private Map<UUID, Record> records;
    
    public DataAccess() {
        this.records = new HashMap<>();
    }

    public void create(final Data data) throws ExistingRecordException {
        final Record record = this.records.get(data.uuid());
        if (record != null) throw new ExistingRecordException("Record already exists (UUID=" + data.uuid() + ")");
        this.records.put(data.uuid(), convertDataToRecord(data));
    }

    public void update(final Data data) throws UnexistingRecordException {
        final Record record = this.records.get(data.uuid());
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + data.uuid() + ")");
        this.records.put(data.uuid(), convertDataToRecord(data));
    }

    public Data read(final UUID uuid) throws UnexistingRecordException {
        final Record record = this.records.get(uuid);
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + uuid + ")");
        return convertRecordToDate(record);
    }

    public void delete(final UUID uuid) throws UnexistingRecordException {
        final Record record = this.records.get(uuid);
        if (record == null) throw new UnexistingRecordException("Unkwown record (UUID=" + uuid + ")");
        this.records.remove(uuid);
    }

    private Record convertDataToRecord(final Data data) {
        return new Record(data.uuid(), data.clientTimeStamp(), ZonedDateTime.now(), data.humanId(), data.humanDescription());
    }

    private Data convertRecordToDate(final Record record) {
        return new Data(record.uuid(), record.clientTimeStamp(), record.humanId(), record.humanDescription());
    }
}
