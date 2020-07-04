package tokenbooking.model;

public enum UserRoleEnum {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN");
    public final String label;

    private UserRoleEnum(String label) {
        this.label = label;
    }
}
