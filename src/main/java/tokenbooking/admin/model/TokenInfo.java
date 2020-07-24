package tokenbooking.admin.model;

import java.time.LocalTime;
import java.util.UUID;

public class TokenInfo {
    Integer tokenNumber;
    String userName;
    UUID bookingId;

    private LocalTime fromTime;

    private LocalTime toTime;

    boolean hasMoreTokens;

    public TokenInfo(Integer tokenNumber, String userName, UUID bookingId) {
        this.tokenNumber = tokenNumber;
        this.userName = userName;
        this.bookingId = bookingId;
    }

    public TokenInfo() {

    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public boolean isHasMoreTokens() {
        return hasMoreTokens;
    }

    public void setHasMoreTokens(boolean hasMoreTokens) {
        this.hasMoreTokens = hasMoreTokens;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }
}
