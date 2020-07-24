package tokenbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import tokenbooking.admin.comparator.DateAndFromTimeComparator;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class AdminSessionSummary implements DateAndFromTimeComparator {
    private UUID sessionId;
    private Integer availableTokens;
    private Integer submittedTokens;
    private Integer bookedTokens;
    private Integer completedTokens;
    private DayOfWeek day;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime fromTime;

    @JsonFormat(pattern = "KK:mm a")
    private LocalTime toTime;

    public AdminSessionSummary() {}

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(Integer availableTokens) {
        this.availableTokens = availableTokens;
    }

    public Integer getSubmittedTokens() {
        return submittedTokens;
    }

    public void setSubmittedTokens(Integer submittedTokens) {
        this.submittedTokens = submittedTokens;
    }

    public Integer getBookedTokens() {
        return bookedTokens;
    }

    public void setBookedTokens(Integer bookedTokens) {
        this.bookedTokens = bookedTokens;
    }

    public Integer getCompletedTokens() {
        return completedTokens;
    }

    public void setCompletedTokens(Integer completedTokens) {
        this.completedTokens = completedTokens;
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

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
