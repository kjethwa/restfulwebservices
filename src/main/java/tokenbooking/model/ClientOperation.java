package tokenbooking.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tokenbooking.jsoncustomparser.LocalTimeDeserializer;
import tokenbooking.jsoncustomparser.LocalTimeSerializer;

import javax.persistence.*;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
public class ClientOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operationId;
    private DayOfWeek day;
    private Integer noOfTokens;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime fromTime;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime toTime;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
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
