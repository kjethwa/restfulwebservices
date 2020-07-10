package tokenbooking.model;

public enum BookingStatus {
    BOOKED("BOOKED"),
    SUBMITTED("SUBMITTED"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED"),
    EXPIRED("EXPIRED"),
    CANCELLED_BY_ADMIN("CANCELLED_BY_ADMIN");

    private String value;

    BookingStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
