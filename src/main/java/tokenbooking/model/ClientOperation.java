package tokenbooking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClientOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long operationId;
    private String day;
    private String noOfTokens;
    private String fromTime;
    private String toTime;

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

    public String getNoOfTokens() {
        return noOfTokens;
    }

    public void setNoOfTokens(String noOfTokens) {
        this.noOfTokens = noOfTokens;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}
