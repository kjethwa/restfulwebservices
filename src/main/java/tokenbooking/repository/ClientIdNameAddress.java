package tokenbooking.repository;

import org.springframework.beans.factory.annotation.Value;

public interface ClientIdNameAddress {
    Long getClientId();

    String getClientName();

    String getHouseNo();

    String getStreet();

    String getStreet1();

    String getStreet2();

    String getStreet3();

    String getStreet4();

    String getCity();

    String getPincode();
}
