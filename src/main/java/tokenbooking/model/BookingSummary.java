package tokenbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import tokenbooking.admin.comparator.DateAndFromTimeComparator;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class BookingSummary implements DateAndFromTimeComparator {

    private UUID clientId;
    private String clientName;
    private UUID sessionId;
    private UUID bookingId;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime fromTime;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime toTime;

    private Integer tokenNumber;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime recommendedTime;

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

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public LocalTime getRecommendedTime() {
        return recommendedTime;
    }

    public void setRecommendedTime(LocalTime recommendedTime) {
        this.recommendedTime = recommendedTime;
    }
}
