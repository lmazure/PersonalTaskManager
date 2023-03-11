package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Task as recorded by the server
 */
public record Record(UUID uuid, ZonedDateTime clientTimeStamp, ZonedDateTime serverTimeStamp, String humanId, String humanDescription) {

    public Record {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(clientTimeStamp);
        Objects.requireNonNull(serverTimeStamp);
        Objects.requireNonNull(humanId);
        Objects.requireNonNull(humanDescription);
    }
}
