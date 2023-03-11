package fr.mazure.personaltaskmanager;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Task data exchanged between the client and the server
 */
public record Data(UUID uuid, ZonedDateTime clientTimeStamp, String humanId, String humanDescription) {
    public Data {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(clientTimeStamp);
        Objects.requireNonNull(humanId);
        Objects.requireNonNull(humanDescription);
    }
}
