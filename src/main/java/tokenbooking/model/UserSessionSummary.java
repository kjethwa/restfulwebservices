package tokenbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import tokenbooking.admin.comparator.DateAndFromTimeComparator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class UserSessionSummary implements DateAndFromTimeComparator {

    private UUID sessionId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private Integer availableToken;
    private Integer nextAvailableToken;
    private Integer noOfTokens;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime fromTime;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime toTime;

    private boolean booked;

    private Integer tokenNumber;

    public boolean getBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAvailableToken() {
        return availableToken;
    }

    public void setAvailableToken(Integer availableToken) {
        this.availableToken = availableToken;
    }

    public Integer getNextAvailableToken() {
        return nextAvailableToken;
    }

    public void setNextAvailableToken(Integer nextAvailableToken) {
        this.nextAvailableToken = nextAvailableToken;
    }

    public Integer getNoOfTokens() {
        return noOfTokens;
    }

    public void setNoOfTokens(Integer noOfTokens) {
        this.noOfTokens = noOfTokens;
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
