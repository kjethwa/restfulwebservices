package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails,Long> {

    UserDetails findByLoginId(String loginId);
    UserDetails findByPhoneNumber(String phoneNumber);
}
