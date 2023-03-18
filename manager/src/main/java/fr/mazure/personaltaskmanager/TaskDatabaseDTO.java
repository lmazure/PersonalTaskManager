package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Task data exchanged between the business layer and the sdata access layer
 */
public record TaskDatabaseDto(UUID uuid, ZonedDateTime clientTimeStamp, String humanId, String humanDescription) {
    public TaskDatabaseDto {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(clientTimeStamp);
        Objects.requireNonNull(humanId);
        Objects.requireNonNull(humanDescription);
    }
}
