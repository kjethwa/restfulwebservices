package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.ClientOperation;

import java.util.UUID;

public interface ClientOperationRepository extends JpaRepository<ClientOperation, UUID> {
}
