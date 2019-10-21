package tokenbooking.admin.model;

public class TokenInfo {
    Integer tokenNumber;
    String userName;
    Long bookingId;
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
}
