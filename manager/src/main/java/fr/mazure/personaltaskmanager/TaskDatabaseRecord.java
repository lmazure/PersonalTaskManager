package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Task as recorded by the database
 */
public record TaskDatabaseRecord(UUID uuid, ZonedDateTime clientTimeStamp, ZonedDateTime serverTimeStamp, String humanId, String humanDescription) {
    public TaskDatabaseRecord {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(clientTimeStamp);
        Objects.requireNonNull(serverTimeStamp);
        Objects.requireNonNull(humanId);
        Objects.requireNonNull(humanDescription);
    }
}
