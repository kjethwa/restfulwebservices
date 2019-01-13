package Utils;

import tokenbooking.model.ClientOperation;
import tokenbooking.model.SessionDetails;

import static tokenbooking.model.Constants.START_TOKEN_NUMBER;

public class HelperUtil {
    public static void copyClientOperationDetails(ClientOperation clientOperation, SessionDetails sessionDetails) {
        sessionDetails.setFromTime(clientOperation.getFromTime());
        sessionDetails.setToTime(clientOperation.getToTime());
        sessionDetails.setNoOfTokens(clientOperation.getNoOfTokens());
        sessionDetails.setAvailableToken(clientOperation.getNoOfTokens());
        sessionDetails.setNextAvailableToken(START_TOKEN_NUMBER);
    }
}
