package fr.mazure.personaltaskmanager;

import java.util.Objects;
import java.util.UUID;

/**
 * Task data exchanged between the client and the server
 */
public record TaskClientDto(UUID uuid, String clientTimeStamp, String humanId, String humanDescription) {
    public TaskClientDto {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(clientTimeStamp);
        Objects.requireNonNull(humanId);
        Objects.requireNonNull(humanDescription);
    }
}
