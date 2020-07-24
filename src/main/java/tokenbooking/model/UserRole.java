package tokenbooking.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class UserRole {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
