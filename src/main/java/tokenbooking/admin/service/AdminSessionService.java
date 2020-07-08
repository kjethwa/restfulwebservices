package tokenbooking.admin.service;

import org.apache.catalina.User;
import tokenbooking.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import tokenbooking.admin.exception.AdminException;
import tokenbooking.admin.model.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.*;
import tokenbooking.repository.*;
import tokenbooking.service.BookingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tokenbooking.model.Constants.*;

@Service
public class AdminSessionService {

    Logger LOG = LoggerFactory.getLogger(AdminSessionService.class);

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    @Autowired
    ClientOperationRepository clientOperationRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    BookingService bookingService;

    @Autowired
    ClientRepository clientRepository;

    @Transactional()
    public void startSession(Long sessionId) throws AdminException {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);
        if (sessionDetails == null) {
            throw new AdminException("Session not found " + sessionId);
        } else if (INPROGRESS.equalsIgnoreCase(sessionDetails.getStatus())) {
            throw new AdminException("Session already started");
        } else if (isClientHasOtherSessionInProgress(sessionDetails)) {
            LOG.info("Can not start session other session is already in progress with session id {}", sessionId);
            throw new AdminException("Can not start session other session is already in progress.");
        }

        if (CREATED.equals(sessionDetails.getStatus())) {
            copyClientOperationDetails(sessionDetails);
        } else {
            sessionDetails.setStatus(INPROGRESS);
            sessionDetailsRepository.save(sessionDetails);
        }
    }

    @Transactional()
    public TokenInfo getNextToken(Long sessionId) throws AdminException {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);
        if (sessionDetails == null) {
            throw new AdminException("Session not found " + sessionId);
        }

        if (!INPROGRESS.equals(sessionDetails.getStatus())) {
            throw new AdminException("Session not started " + sessionId);
        } else {
            BookingDetails previousBookingDetails = completePreviousToken(sessionId);
            BookingDetails nextBooking = bookingService.getSubmittedBookingOfLeastTokenNumber(sessionId);
            TokenInfo tokenInfo = null;

            if (nextBooking != null) {
                setSequenceNumber(previousBookingDetails, nextBooking);
                tokenInfo = getTokenInfo(nextBooking);
                tokenInfo.setHasMoreTokens(true);
            } else if (previousBookingDetails != null) {
                tokenInfo = getTokenInfo(previousBookingDetails);
                tokenInfo.setHasMoreTokens(false);
            }

            return tokenInfo;
        }
    }

    @Transactional()
    public AdminSummary getAllSessionDetails(String loginId) {
        UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
        Long clientId = userDetails.getClientId();
        LOG.debug("Getting all session details of clientId {}", clientId);
        List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetweenAndStatusIn(clientId, HelperUtil.getCurrentDate(), HelperUtil.getEndDate(), Arrays.asList(ACTIVE, INPROGRESS)));
        LOG.debug("Number of sessions found = {} ", allAvailableSessions.size());
        return getAdminSummary(clientId, allAvailableSessions);
    }

    @Transactional()
    public void completeSession(Long sessionId) {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);

        if (COMPLETED.equals(sessionDetails.getStatus())) {
           return;
        }

        if (ACTIVE.equals(sessionDetails.getStatus()) || INPROGRESS.equals(sessionDetails.getStatus())) {
            bookingRepository.updateBookingStatusOfSessionId(CANCELLED_BY_ADMIN, sessionDetails.getSessionId(), Arrays.asList(BOOKED, SUBMITTED));
        }

        setCompletionValueAndCompleteSession(sessionDetails);
    }

    @Transactional()
    public AdminSummary getActiveSession(String loginId) {
        UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
        Long clientId = userDetails.getClientId();

        List<SessionDetails> sessionDetails = (List<SessionDetails>) sessionDetailsRepository.findByClientIdAndStatusIn(clientId, Arrays.asList(INPROGRESS));

        if (sessionDetails.isEmpty()) {
            throw new AdminException("No Active session found");
        } else {
            return getAdminSummary(clientId, sessionDetails);
        }
    }

    public TokenInfo getLastToken(Long sessionId) {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);
        if (!INPROGRESS.equals(sessionDetails.getStatus())) {
            throw new AdminException("Session not yet started.");
        }

        BookingDetails bookingDetails = bookingService.getBookingOfLastSequenceNumber(sessionId);

        TokenInfo tokenInfo = bookingDetails == null ? null : getTokenInfo(bookingDetails);

        if (tokenInfo != null) {
            tokenInfo.setHasMoreTokens(true);
        }
        return tokenInfo;
    }

    private void setCompletionValueAndCompleteSession(SessionDetails sessionDetails) {
        sessionDetails.setStatus(COMPLETED);
        sessionDetails.setNextAvailableToken(ZERO);
        sessionDetails.setAvailableToken(ZERO);
        sessionDetailsRepository.save(sessionDetails);
    }

    private boolean isClientHasOtherSessionInProgress(SessionDetails sessionDetails) {
        List<SessionDetails> result = new ArrayList<>(sessionDetailsRepository.findByClientIdAndStatusIn(sessionDetails.getClientId(), Arrays.asList(INPROGRESS)));

        return !result.isEmpty();
    }

    private AdminSummary getAdminSummary(Long clientId, List<SessionDetails> sessionDetailsList) {
        AdminSummary adminSummary = new AdminSummary();

        List<AdminSessionSummary> adminSessionSummaries = sessionDetailsList.stream().map(this::getAdminSessionSummary).collect(Collectors.toList());
        Client client = clientRepository.findOne(clientId);
        adminSummary.setClientName(client.getClientName());
        adminSummary.setClientId(clientId);
        adminSummary.setAdminSessionSummaryList(adminSessionSummaries);

        return adminSummary;
    }

    private AdminSessionSummary getAdminSessionSummary(SessionDetails sessionDetails) {
        AdminSessionSummary adminSessionSummary = new AdminSessionSummary();
        adminSessionSummary.setAvailableTokens(sessionDetails.getAvailableToken());
        adminSessionSummary.setBookedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), BOOKED).intValue());
        adminSessionSummary.setSubmittedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), SUBMITTED).intValue());
        adminSessionSummary.setCompletedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), COMPLETED).intValue());
        adminSessionSummary.setSessionId(sessionDetails.getSessionId());
        adminSessionSummary.setDate(sessionDetails.getDate());
        adminSessionSummary.setFromTime(sessionDetails.getFromTime());
        adminSessionSummary.setToTime(sessionDetails.getToTime());
        adminSessionSummary.setStatus(sessionDetails.getStatus());

        ClientOperation clientOperation = clientOperationRepository.findOne(sessionDetails.getOperationId());
        adminSessionSummary.setDay(clientOperation.getDay());

        return adminSessionSummary;
    }

    private TokenInfo getTokenInfo(BookingDetails nextBooking) {
        UserDetails userDetails = userDetailsRepository.getOne(nextBooking.getUserId());

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setBookingId(nextBooking.getBookingId());
        tokenInfo.setTokenNumber(nextBooking.getTokenNumber());
        tokenInfo.setUserName(userDetails.getFirstName() + " " + userDetails.getLastName());

        return tokenInfo;
    }

    private void setSequenceNumber(BookingDetails previousBookingDetails, BookingDetails nextBooking) {
        if (previousBookingDetails != null && nextBooking != null)
            nextBooking.setSequenceNumber(previousBookingDetails.getSequenceNumber() + 1);
        else if (nextBooking != null)
            nextBooking.setSequenceNumber(1);

        bookingRepository.save(nextBooking);
    }

    private BookingDetails completePreviousToken(Long sessionId) {
        BookingDetails bookingDetails = bookingService.getBookingOfLastSequenceNumber(sessionId);

        if (bookingDetails != null && !COMPLETED.equalsIgnoreCase(bookingDetails.getStatus())) {
            bookingDetails.setStatus(COMPLETED);
            bookingRepository.save(bookingDetails);
        }

        return bookingDetails;
    }

    private void copyClientOperationDetails(SessionDetails sessionDetails) {
        ClientOperation clientOperation = clientOperationRepository.findOne(sessionDetails.getOperationId());
        HelperUtil.copyClientOperationDetails(clientOperation, sessionDetails);
    }

}
