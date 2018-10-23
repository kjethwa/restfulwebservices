package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
