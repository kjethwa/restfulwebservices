package tokenbooking.admin.model;

import java.time.LocalTime;

public class TokenInfo {
    Integer tokenNumber;
    String userName;
    Long bookingId;

    private LocalTime fromTime;

    private LocalTime toTime;

    boolean hasMoreTokens;

    public TokenInfo(Integer tokenNumber, String userName, Long bookingId) {
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

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
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
