package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.ClientLoginDetail;

public interface ClientLoginDetailRepository extends JpaRepository<ClientLoginDetail,Long> {

    ClientLoginDetail findByUserName(String username);

}
