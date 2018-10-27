package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tokenbooking.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long> ,JpaSpecificationExecutor<Client> {

}
