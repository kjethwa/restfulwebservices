package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tokenbooking.model.Client;
import tokenbooking.model.ClientSearchDetails;

import java.util.Collection;

public interface ClientRepository extends JpaRepository<Client,Long> ,JpaSpecificationExecutor<Client> {

}
