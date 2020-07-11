package tokenbooking.admin.model;

public class AuthenticationResponse {
    private String jwt;
    private String errorMessage;
    private String userName;
    private String role;

    public AuthenticationResponse(String jwt,String userName,String role,String errorMessage) {
        this.jwt = jwt;
        this.userName = userName;
        this.role = role;
        this.errorMessage = errorMessage;
    }

    public AuthenticationResponse(String jwt,String err) {
        this.jwt = jwt;
        this.errorMessage = err;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
