package tokenbooking.admin.service;

import Utils.HelperUtil;
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
        }

        if (CREATED.equals(sessionDetails.getStatus())) {
            copyClientOperationDetails(sessionDetails);
        }

        if (INPROGRESS.equalsIgnoreCase(sessionDetails.getStatus())) {
            throw new AdminException("Session already started");
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

            if (nextBooking != null) {
                setSequenceNumber(previousBookingDetails, nextBooking);

                return getTokenInfo(nextBooking);
            } else {
                return null;
            }
        }
    }

    @Transactional()
    public List<AdminSessionSummary> getAllSessionDetails(Long clientId) {
        LOG.debug("Getting all session details of clientId {}", clientId);
        List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetweenAndStatusIn(clientId, HelperUtil.getCurrentDate(), HelperUtil.getEndDate(), Arrays.asList(ACTIVE, INPROGRESS)));
        LOG.debug("Number of sessions found = {} ", allAvailableSessions.size());
        return allAvailableSessions.stream().map(this::getAdminSessionSummary).collect(Collectors.toList());
    }

    private AdminSessionSummary getAdminSessionSummary(SessionDetails sessionDetails) {
        AdminSessionSummary adminSessionSummary = new AdminSessionSummary();
        adminSessionSummary.setAvailableTokens(sessionDetails.getAvailableToken());
        adminSessionSummary.setBookedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), BOOKED).intValue());
        adminSessionSummary.setSubmittedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), SUBMITTED).intValue());
        adminSessionSummary.setCompletedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), COMPLETED).intValue());
        adminSessionSummary.setClientId(sessionDetails.getClientId());
        adminSessionSummary.setSessionId(sessionDetails.getSessionId());
        adminSessionSummary.setDate(sessionDetails.getDate());
        adminSessionSummary.setFromTime(sessionDetails.getFromTime());
        adminSessionSummary.setToTime(sessionDetails.getToTime());

        Client client = clientRepository.findOne(sessionDetails.getClientId());
        adminSessionSummary.setClientName(client.getClientName());

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
