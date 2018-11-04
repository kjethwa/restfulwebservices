package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails,Long> {
}
