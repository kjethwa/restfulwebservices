package tokenbooking.service;

import com.sun.corba.se.spi.extension.ZeroPortPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.Client;
import tokenbooking.model.ClientAndSessionDetails;
import tokenbooking.model.ClientOperation;
import tokenbooking.model.SessionDetails;
import tokenbooking.repository.SessionDetailsRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static tokenbooking.model.Constants.*;

@Service
public class SessionService {

    @Autowired
    ClientService clientService;

    @Autowired
    ClientOperationService clientOperationService;

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    public ClientAndSessionDetails getSessionDetailsOfClientWithClientNameAndAddressSummary(Long clientId) throws Exception {
        ClientAndSessionDetails clientAndSessionDetails = new ClientAndSessionDetails();
        clientAndSessionDetails.setClientIdNameAddress(clientService.getClientNameAndAddressSummary(clientId));
        List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetweenAndStatusIn(clientId, getCurrentDate(), getEndDate(), Arrays.asList(ACTIVE, INPROGRESS)));

        checkAllSessionIsPresentOrCreate(allAvailableSessions,clientId);

        checkIsSessionHasAllFieldsOrCopyFromClientDetails(allAvailableSessions);

        clientAndSessionDetails.setSessions(allAvailableSessions);

        return clientAndSessionDetails;
    }

    public SessionDetails save(SessionDetails sessionDetails) {
        return sessionDetailsRepository.save(sessionDetails);
    }

    public SessionDetails getSessionDetails(Long sessionId) {
        return sessionDetailsRepository.findOne(sessionId);
    }

    public Integer getNextAvailableToken(Long sessionId) throws Exception {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);
        if(sessionDetails.getStatus().equals(CREATED)){
            return START_TOKEN_NUMBER;
        }

        if (sessionDetails.getAvailableToken().equals(ZERO)) {
            throw new Exception("All tokens are booken.");
        }

        return sessionDetails.getNextAvailableToken();
    }

    private void checkAllSessionIsPresentOrCreate(List<SessionDetails> allAvailableSessions, Long clientId) {
        Client client = clientService.getClientById(clientId);
        List<ClientOperation> daysOfOperation = client.getDaysOfOperation();
        Map<DayOfWeek, List<ClientOperation>> mapOfDaysOfOperation = new HashMap<>();
        for (ClientOperation clientOperation : daysOfOperation) {
            if (mapOfDaysOfOperation.get(clientOperation.getDay()) == null) {
                mapOfDaysOfOperation.put(clientOperation.getDay(), new ArrayList<>());
            }
            mapOfDaysOfOperation.get(clientOperation.getDay()).add(clientOperation);
        }

        //create Map for currently available sessions
        Map<LocalDate, List<SessionDetails>> mapOfSessions = new TreeMap<>();
        for (SessionDetails sessionDetails : allAvailableSessions) {
            if (mapOfSessions.get(sessionDetails.getDate()) == null) {
                mapOfSessions.put(sessionDetails.getDate(), new ArrayList<>());
            }
            mapOfSessions.get(sessionDetails.getDate()).add(sessionDetails);
        }

        //check the session if not present for the day create one
        LocalDate nextDate = getCurrentDate();
        for (int i = 0; i < MAX_DAYS_OF_SESSION; i++) {
            if (mapOfDaysOfOperation.get(nextDate.getDayOfWeek()) != null && mapOfSessions.get(nextDate) == null) {
                createSession(allAvailableSessions, nextDate, mapOfDaysOfOperation.get(nextDate.getDayOfWeek()), clientId);
            }
            nextDate = nextDate.plusDays(1);
        }

    }

    private void checkIsSessionHasAllFieldsOrCopyFromClientDetails(List<SessionDetails> allAvailableSessions) throws Exception {
        Iterator<SessionDetails> iterator = allAvailableSessions.iterator();
        while(iterator.hasNext()) {
            SessionDetails sessionDetails = iterator.next();

            if (sessionDetails.getStatus() == null) {
                throw new Exception("Invalid session status");
            }

            if (CREATED.equals(sessionDetails.getStatus())) {
                ClientOperation clientOperation = clientOperationService.getClientOperation(sessionDetails.getOperationId());
                sessionDetails.setNoOfTokens(clientOperation.getNoOfTokens());
                sessionDetails.setToTime(clientOperation.getToTime());
                sessionDetails.setFromTime(clientOperation.getFromTime());
                sessionDetails.setAvailableToken(clientOperation.getNoOfTokens());
            }

            if(!validateSessionStatus(sessionDetails)){
                iterator.remove();
            }
        }
    }

    private boolean validateSessionStatus(SessionDetails sessionDetails) {
        if(sessionDetails.getDate().isAfter(getCurrentDate())){
            return true;
        }
        if(sessionDetails.getDate().isEqual(getCurrentDate()) && sessionDetails.getToTime().isAfter(getCurrentTime())){
            return true;
        }
        return false;
    }

    private void createSession(List<SessionDetails> allAvailableSessions, LocalDate nextDate, List<ClientOperation> clientOperations, Long clientId) {
        for (ClientOperation clientOperation : clientOperations) {
            SessionDetails sessionDetails = new SessionDetails();
            sessionDetails.setClientId(clientId);
            sessionDetails.setDate(nextDate);
            sessionDetails.setOperationId(clientOperation.getOperationId());
            sessionDetails.setStatus(CREATED);

            allAvailableSessions.add(sessionDetailsRepository.save(sessionDetails));
        }
    }

    private LocalDate getEndDate() {
        LocalDate today = getCurrentDate();     //Today
        LocalDate weekEndDate = today.plusDays(MAX_DAYS_OF_SESSION);
        return weekEndDate;
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    private LocalTime getCurrentTime(){
        return LocalTime.now();
    }

}
