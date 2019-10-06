package tokenbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tokenbooking.jsoncustomparser.LocalTimeDeserializer;
import tokenbooking.jsoncustomparser.LocalTimeSerializer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class AdminSessionSummary {
    private Long sessionId;
    private Integer availableTokens;
    private Integer submittedTokens;
    private Integer bookedTokens;
    private Integer completedTokens;
    private Long clientId;
    private String clientName;
    private DayOfWeek day;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime fromTime;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime toTime;

    public AdminSessionSummary() {}

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }
}
