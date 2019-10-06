package Utils;

import tokenbooking.model.ClientOperation;
import tokenbooking.model.SessionDetails;

import java.time.LocalDate;
import java.time.LocalTime;

import static tokenbooking.model.Constants.MAX_DAYS_OF_SESSION;
import static tokenbooking.model.Constants.START_TOKEN_NUMBER;

public class HelperUtil {
    public static void copyClientOperationDetails(ClientOperation clientOperation, SessionDetails sessionDetails) {
        sessionDetails.setFromTime(clientOperation.getFromTime());
        sessionDetails.setToTime(clientOperation.getToTime());
        sessionDetails.setNoOfTokens(clientOperation.getNoOfTokens());
        sessionDetails.setAvailableToken(clientOperation.getNoOfTokens());
        sessionDetails.setNextAvailableToken(START_TOKEN_NUMBER);
    }

    public static LocalDate getEndDate() {
        LocalDate today = getCurrentDate();     //Today
        LocalDate weekEndDate = today.plusDays(MAX_DAYS_OF_SESSION);
        return weekEndDate;
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalTime getCurrentTime(){
        return LocalTime.now();
    }
}
