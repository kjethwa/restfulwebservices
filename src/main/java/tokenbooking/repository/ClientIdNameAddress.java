package tokenbooking.repository;

import java.util.UUID;

public interface  ClientIdNameAddress {
    UUID getClientId();

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
