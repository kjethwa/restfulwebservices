package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.ClientOperation;

public interface ClientOperationRepository extends JpaRepository<ClientOperation,Long> {
}
