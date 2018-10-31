package tokenbooking.model;

public class ClientSearchDetails {

    private String clientName;
    private String ownerFirstName;
    private String ownerLastName;
    private String clientCategory;
    private String status;
    private String state;
    private String city;

    public ClientSearchDetails(String clientCategory, String clientName, String ownerFirstName, String ownerLastName, String city, String state, String status) {
        this.clientCategory = clientCategory;
        this.clientName = clientName;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.city = city;
        this.state = state;
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
