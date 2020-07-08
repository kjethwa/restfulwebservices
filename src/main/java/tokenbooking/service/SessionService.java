package tokenbooking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tokenbooking.comparator.UserSessionSummaryComparator;
import tokenbooking.model.*;
import tokenbooking.repository.BookingRepository;
import tokenbooking.repository.ClientNameAndId;
import tokenbooking.repository.SessionDetailsRepository;
import tokenbooking.repository.UserDetailsRepository;
import tokenbooking.utils.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static tokenbooking.model.Constants.*;

@Service
public class SessionService {

    private static Logger LOG = LoggerFactory.getLogger(SessionService.class);

    @Autowired
    ClientService clientService;

    @Autowired
    ClientOperationService clientOperationService;

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    public ClientAndSessionDetails getSessionDetailsOfClientWithClientNameAndAddressSummary(Long clientId, String loginId) throws Exception {
        UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
        ClientAndSessionDetails clientAndSessionDetails = new ClientAndSessionDetails();
        clientAndSessionDetails.setClientIdNameAddress(clientService.getClientNameAndAddressSummary(clientId));
        List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetweenAndStatusIn(clientId, HelperUtil.getCurrentDate(), HelperUtil.getEndDate(), Arrays.asList(CREATED, ACTIVE, INPROGRESS)));

        List<UserSessionSummary> userSessionSummaries = checkIsSessionHasAllFieldsOrCopyFromClientDetails(allAvailableSessions, userDetails.getUserId());
        userSessionSummaries.sort(new UserSessionSummaryComparator());

        clientAndSessionDetails.setSessions(userSessionSummaries);

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

}
