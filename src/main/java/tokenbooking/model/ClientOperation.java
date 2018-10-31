package tokenbooking.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import tokenbooking.jsoncustomparser.LocalTimeDeserializer;
import tokenbooking.jsoncustomparser.LocalTimeSerializer;

import javax.persistence.*;
import java.sql.Time;

@Entity
public class ClientOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operationId;
    private String day;
    private Integer noOfTokens;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private Time fromTime;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private Time toTime;

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

    public Time getFromTime() {
        return fromTime;
    }

    public void setFromTime(Time fromTime) {
        this.fromTime = fromTime;
    }

    public Time getToTime() {
        return toTime;
    }

    public void setToTime(Time toTime) {
        this.toTime = toTime;
    }
}
