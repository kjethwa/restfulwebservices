package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.UserRole;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    List<UserRole> findByUserId(UUID userId);

}
