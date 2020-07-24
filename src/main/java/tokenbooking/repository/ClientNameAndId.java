package tokenbooking.repository;

import java.util.UUID;

public interface ClientNameAndId {
    UUID getClientId();

    String getClientName();
}
