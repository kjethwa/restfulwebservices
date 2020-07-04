package tokenbooking.admin.model;

public class AuthenticationResponse {
    private String jwt;
    private String errorMessage;
    private String userName;

    public AuthenticationResponse(String jwt,String userName,String errorMessage) {
        this.jwt = jwt;
        this.userName = userName;
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
}
