package tokenbooking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.time.LocalTime;

@Entity
public class ClientOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operationId;
    private String day;
    private Integer noOfTokens;
    @DateTimeFormat(pattern = "hh:mm:ss")
    private LocalTime fromTime;
    @DateTimeFormat(pattern = "hh:mm:ss")
    private LocalTime toTime;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
