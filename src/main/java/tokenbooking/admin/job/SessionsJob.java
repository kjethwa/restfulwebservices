package tokenbooking.admin.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tokenbooking.admin.service.AdminSessionService;
import tokenbooking.model.Client;
import tokenbooking.model.ClientOperation;
import tokenbooking.model.SessionDetails;
import tokenbooking.model.SessionStatus;
import tokenbooking.repository.ClientNameAndId;
import tokenbooking.repository.SessionDetailsRepository;
import tokenbooking.service.ClientService;
import tokenbooking.utils.HelperUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static tokenbooking.model.Constants.*;
import static tokenbooking.model.Constants.MAX_DAYS_OF_SESSION;

@Service
public class SessionsJob {

    @Autowired
    ClientService clientService;

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    @Autowired
    AdminSessionService adminSessionService;

    private static Logger LOG = LoggerFactory.getLogger(SessionsJob.class);

    @Scheduled(cron = "0 15/60 * * * *")
    @Transactional()
    public void checkAllSessionIsPresentOrCreate() {
        LOG.info("Starting cron job");
        List<ClientNameAndId> clientNameAndIds = clientService.getListOfAllActiveClients();
        Iterator<ClientNameAndId> iterator = clientNameAndIds.iterator();
        while(iterator.hasNext()) {
            ClientNameAndId clientNameAndId = iterator.next();
            Long clientId = clientNameAndId.getClientId();
            Integer createdSessionCount = 0 ;
            List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetween(clientId, HelperUtil.getCurrentDate(), HelperUtil.getEndDate()));
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
                    createdSessionCount += clientOperationsToBeCreated.size();
                }

                nextDate = nextDate.plusDays(1);
            }
            LOG.info("Created " + createdSessionCount + " sessions for client " + clientId);
        }
        LOG.info("Finished cron job");
    }

    @Scheduled(cron = "0 13/60 * * * *")
    @Transactional
    public void cancelOrCompleteOldSessions() {
        LOG.info("Starting cancel or complete old sessions job");
        List<ClientNameAndId> clientNameAndIds = clientService.getListOfAllActiveClients();
        Iterator<ClientNameAndId> iterator = clientNameAndIds.iterator();
        while (iterator.hasNext()) {
            ClientNameAndId clientNameAndId = iterator.next();
            List<SessionDetails> sessionDetailsList = sessionDetailsRepository.findByClientIdAndDateBeforeAndStatusIn(clientNameAndId.getClientId(), HelperUtil.getCurrentDate(), Arrays.asList(SessionStatus.ACTIVE, SessionStatus.INPROGRESS));
            LOG.info("Found {} sessions", sessionDetailsList.size());
            sessionDetailsList.stream().forEach(sessionDetails -> {
                if (sessionDetails.getStatus() == SessionStatus.ACTIVE || sessionDetails.getStatus() == SessionStatus.INPROGRESS) {
                    adminSessionService.completeSession(sessionDetails.getSessionId());
                }
                /*else if (sessionDetails.getStatus().equalsIgnoreCase(CREATED)) {
                    // TODO : Session with CREATED status is as good as dummy entry, can be deleted.
                }*/
            });
        }

        LOG.info("Finished cancel or complete old sessions job");
    }

    private void createSession(List<SessionDetails> allAvailableSessions, LocalDate nextDate, List<ClientOperation> clientOperations, Long clientId) {
        for (ClientOperation clientOperation : clientOperations) {
            SessionDetails sessionDetails = new SessionDetails();
            sessionDetails.setClientId(clientId);
            sessionDetails.setDate(nextDate);
            sessionDetails.setOperationId(clientOperation.getOperationId());
            sessionDetails.setStatus(SessionStatus.ACTIVE);
            sessionDetails.setFromTime(clientOperation.getFromTime());
            sessionDetails.setToTime(clientOperation.getToTime());
            sessionDetails.setNoOfTokens(clientOperation.getNoOfTokens());
            sessionDetails.setAvailableToken(clientOperation.getNoOfTokens());
            sessionDetails.setNextAvailableToken(START_TOKEN_NUMBER);

            allAvailableSessions.add(sessionDetailsRepository.save(sessionDetails));
        }
    }
}
