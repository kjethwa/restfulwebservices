package tokenbooking.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Client {

    @Id
    @GeneratedValue()
    private UUID clientId;
    private String clientName;
    private String ownerFirstName;
    private String ownerLastName;
    private String clientCategory;
    private String status;
    private Integer prebookingHours;
    private String houseNo;
    private String street;
    private String street1;
    private String street2;
    private String street3;
    private String street4;
    private String pincode;
    private String country;
    private String state;
    private String city;
    private String latitude;
    private String longitude;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "client_config_id",referencedColumnName = "id")
    private ClientConfigurationSetting clientConfigurationSetting;

    @OneToMany(mappedBy = "client", cascade = {CascadeType.ALL})
    private List<ClientOperation> daysOfOperation;

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

    public Integer getPrebookingHours() {
        return prebookingHours;
    }

    public void setPrebookingHours(Integer prebookingHours) {
        this.prebookingHours = prebookingHours;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet3() {
        return street3;
    }

    public void setStreet3(String street3) {
        this.street3 = street3;
    }

    public String getStreet4() {
        return street4;
    }

    public void setStreet4(String street4) {
        this.street4 = street4;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<ClientOperation> getDaysOfOperation() {
        return daysOfOperation;
    }

    public void setDaysOfOperation(List<ClientOperation> daysOfOperation) {
        this.daysOfOperation = daysOfOperation;
    }

    public ClientConfigurationSetting getClientConfigurationSetting() {
        return clientConfigurationSetting;
    }

    public void setClientConfigurationSetting(ClientConfigurationSetting clientConfigurationSetting) {
        this.clientConfigurationSetting = clientConfigurationSetting;
    }
}
