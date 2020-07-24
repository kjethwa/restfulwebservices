package tokenbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class SessionDetails {

    @Id
    @GeneratedValue()
    private UUID sessionId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private UUID clientId;
    private UUID operationId;
    private Integer availableToken;
    private Integer nextAvailableToken;
    private Integer noOfTokens;

    @Basic
    private LocalTime fromTime;

    @Basic
    private LocalTime toTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionDetails)) return false;
        SessionDetails that = (SessionDetails) o;
        return Objects.equals(getSessionId(), that.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId());
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

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
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

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
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
