package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.UserDetails;

import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {

    UserDetails findByLoginId(String loginId);
    UserDetails findByPhoneNumber(String phoneNumber);
}
