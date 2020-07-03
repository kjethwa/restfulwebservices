package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.comparator.UserSessionSummaryComparator;
import tokenbooking.model.*;
import tokenbooking.repository.BookingRepository;
import tokenbooking.repository.ClientNameAndId;
import tokenbooking.repository.SessionDetailsRepository;
import tokenbooking.utils.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static tokenbooking.model.Constants.*;

@Service
public class SessionService {

    @Autowired
    ClientService clientService;

    @Autowired
    ClientOperationService clientOperationService;

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    @Autowired
    BookingRepository bookingRepository;

    public ClientAndSessionDetails getSessionDetailsOfClientWithClientNameAndAddressSummary(Long clientId,Long userId) throws Exception {
        ClientAndSessionDetails clientAndSessionDetails = new ClientAndSessionDetails();
        clientAndSessionDetails.setClientIdNameAddress(clientService.getClientNameAndAddressSummary(clientId));
        List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetweenAndStatusIn(clientId, HelperUtil.getCurrentDate(), HelperUtil.getEndDate(), Arrays.asList(CREATED, ACTIVE, INPROGRESS)));

        checkAllSessionIsPresentOrCreate(allAvailableSessions,clientId);

        List<UserSessionSummary> userSessionSummaries = checkIsSessionHasAllFieldsOrCopyFromClientDetails(allAvailableSessions,userId);
        userSessionSummaries.sort(new UserSessionSummaryComparator());

        clientAndSessionDetails.setSessions(userSessionSummaries);

        return clientAndSessionDetails;
    }

    public List<ClientAndSessionDetails> getAllSessionDetailsOfAllActiveClients(Long userId) {
        List<ClientAndSessionDetails> clientAndSessionDetails = new ArrayList<>();
        List<ClientNameAndId> clientNameAndIds = clientService.getListOfAllActiveClients();
        clientNameAndIds.stream().forEach(clientNameAndId -> {
            try {
                clientAndSessionDetails.add(this.getSessionDetailsOfClientWithClientNameAndAddressSummary(clientNameAndId.getClientId(), userId));
            } catch (Exception e) {
                // TODO add logger and handle exception
            }
        });
        return clientAndSessionDetails;
    }

    public SessionDetails save(SessionDetails sessionDetails) {
        return sessionDetailsRepository.save(sessionDetails);
    }

    public SessionDetails getSessionDetails(Long sessionId) {
        return sessionDetailsRepository.findOne(sessionId);
    }

    public synchronized Integer getNextAvailableToken(Long sessionId) throws Exception {
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
        LocalDate nextDate = HelperUtil.getCurrentDate();
        for (int i = 0; i <= MAX_DAYS_OF_SESSION; i++) {
            if (mapOfDaysOfOperation.get(nextDate.getDayOfWeek()) != null) {
                Set<Long> presentOperationIds = new HashSet<>();
                if (mapOfSessions.get(nextDate) != null)
                   presentOperationIds = mapOfSessions.get(nextDate).stream().map(SessionDetails::getOperationId).collect(Collectors.toSet());

                Set<Long> finalPresentOperationIds = presentOperationIds;
                List<ClientOperation> clientOperationsToBeCreated = mapOfDaysOfOperation.get(nextDate.getDayOfWeek()).stream().filter(s -> !finalPresentOperationIds.contains(s.getOperationId())).collect(Collectors.toList());

                createSession(allAvailableSessions, nextDate, clientOperationsToBeCreated, clientId);
            }

            nextDate = nextDate.plusDays(1);
        }
    }

    private List<UserSessionSummary> checkIsSessionHasAllFieldsOrCopyFromClientDetails(List<SessionDetails> allAvailableSessions, Long userId) throws Exception {
        Iterator<SessionDetails> iterator = allAvailableSessions.iterator();
        List<UserSessionSummary> userSessionSummaries = new ArrayList<>(allAvailableSessions.size());
        while (iterator.hasNext()) {
            SessionDetails sessionDetails = iterator.next();
            UserSessionSummary userSessionSummary = new UserSessionSummary();
            if (sessionDetails.getStatus() == null) {
                throw new Exception("Invalid session status");
            }

            if (CREATED.equals(sessionDetails.getStatus())) {
                ClientOperation clientOperation = clientOperationService.getClientOperation(sessionDetails.getOperationId());
                userSessionSummary.setNoOfTokens(clientOperation.getNoOfTokens());
                userSessionSummary.setToTime(clientOperation.getToTime());
                userSessionSummary.setFromTime(clientOperation.getFromTime());
                userSessionSummary.setAvailableToken(clientOperation.getNoOfTokens());

                sessionDetails.setToTime(clientOperation.getToTime());
            }
            else{
                userSessionSummary.setNoOfTokens(sessionDetails.getNoOfTokens());
                userSessionSummary.setToTime(sessionDetails.getToTime());
                userSessionSummary.setFromTime(sessionDetails.getFromTime());
                userSessionSummary.setAvailableToken(sessionDetails.getAvailableToken());
            }
            userSessionSummary.setSessionId(sessionDetails.getSessionId());
            userSessionSummary.setDate(sessionDetails.getDate());

            checkIsAlreadyBookedInSession(sessionDetails,userId,userSessionSummary);
            if (validateSessionStatus(sessionDetails)) {
                userSessionSummaries.add(userSessionSummary);
            }
        }

        return userSessionSummaries;
    }

    private void checkIsAlreadyBookedInSession(SessionDetails sessionDetails, Long userId, UserSessionSummary userSessionSummary) throws Exception {
        Collection<BookingDetails> bookingDetailList = bookingRepository.findBySessionIdAndUserIdAndStatusIn(sessionDetails.getSessionId(), userId, Arrays.asList(BOOKED, SUBMITTED));
        if(bookingDetailList.size()>1){
            throw new Exception("Multiple booking found in a session by a user");
        }
        if(bookingDetailList.size() == 1){
            BookingDetails bookingDetails = bookingDetailList.iterator().next();
            userSessionSummary.setBooked(true);
            userSessionSummary.setTokenNumber(bookingDetails.getTokenNumber());
        }
    }

    private boolean validateSessionStatus(SessionDetails sessionDetails) {
        if(!clientService.getClientById(sessionDetails.getClientId()).getDaysOfOperation().stream().anyMatch(s -> s.getOperationId().equals(sessionDetails.getOperationId()))){
            return false;
        }
        if(sessionDetails.getDate().isAfter(HelperUtil.getCurrentDate())){
            return true;
        }
        if(sessionDetails.getDate().isEqual(HelperUtil.getCurrentDate()) && sessionDetails.getToTime().isAfter(HelperUtil.getCurrentTime())){
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
            sessionDetails.setNextAvailableToken(START_TOKEN_NUMBER);

            allAvailableSessions.add(sessionDetailsRepository.save(sessionDetails));
        }
    }

}
