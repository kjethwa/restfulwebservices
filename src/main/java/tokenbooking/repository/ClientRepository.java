package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tokenbooking.model.Client;

import java.util.Collection;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client,UUID> ,JpaSpecificationExecutor<Client> {

    Collection<ClientNameAndId> findByStatus(String status);

    Collection<ClientIdNameAddress> findByStatusAndClientId(String status, UUID clientId);

}
