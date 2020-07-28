package tokenbooking.admin.service;

import tokenbooking.admin.comparator.DateAndFromTimeComparatorImp;
import tokenbooking.admin.comparator.WaitingTimeComparator;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
    public void startSession(UUID sessionId) throws AdminException {
        SessionDetails sessionDetails = sessionDetailsRepository.findById(sessionId).get();
        if (sessionDetails == null) {
            throw new AdminException("Session not found " + sessionId);
        } else if (SessionStatus.INPROGRESS == sessionDetails.getStatus()) {
            throw new AdminException("Session already started");
        } else if (sessionDetails.getDate().isAfter(HelperUtil.getCurrentDate())) {
            throw new AdminException("Can not start future dated session");
        }
        /*else if (isClientHasOtherSessionInProgress(sessionDetails)) {
            LOG.info("Can not start session other session is already in progress with session id {}", sessionId);
            throw new AdminException("Can not start session other session is already in progress.");
        }*/

        sessionDetails.setStatus(SessionStatus.INPROGRESS);
        sessionDetailsRepository.save(sessionDetails);

    }

    @Transactional()
    public TokenInfo getNextToken(UUID sessionId) throws AdminException {
        SessionDetails sessionDetails = sessionDetailsRepository.findById(sessionId).get();
        if (sessionDetails == null) {
            throw new AdminException("Session not found " + sessionId);
        }
        if (SessionStatus.CANCELLED == sessionDetails.getStatus() || SessionStatus.FINISHED == sessionDetails.getStatus()) {
            throw new AdminException("Session already cancelled or finished " + sessionId);
        }
        if (SessionStatus.ACTIVE == sessionDetails.getStatus()) {
            throw new AdminException("Session not started " + sessionId);
        }

        TokenInfo tokenInfo = null;
        if (SessionStatus.INPROGRESS == sessionDetails.getStatus()) {
            BookingDetails previousBookingDetails = completePreviousToken(sessionId);
            BookingDetails nextBooking = getNextAvailableBooking(sessionId);
            if (nextBooking != null) {
                setSequenceNumber(previousBookingDetails, nextBooking);
                tokenInfo = getTokenInfo(nextBooking, sessionDetails);
                tokenInfo.setHasMoreTokens(true);
            } else if (previousBookingDetails != null) {
                tokenInfo = getTokenInfo(previousBookingDetails, sessionDetails);
                tokenInfo.setHasMoreTokens(false);
            }
        }
        return tokenInfo;
    }

    @Transactional()
    public AdminSummary getAllSessionDetails(String loginId) {
        UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
        UUID clientId = userDetails.getClientId();
        LOG.debug("Getting all session details of clientId {}", clientId);
        List<SessionDetails> allAvailableSessions = new ArrayList<>(sessionDetailsRepository.findByClientIdAndDateBetweenAndStatusIn(clientId, HelperUtil.getCurrentDate(), HelperUtil.getEndDate(), Arrays.asList(SessionStatus.ACTIVE, SessionStatus.INPROGRESS)));
        LOG.debug("Number of sessions found = {} ", allAvailableSessions.size());
        return getAdminSummary(clientId, allAvailableSessions);
    }

    @Transactional()
    public void finishSession(UUID sessionId) {
        SessionDetails sessionDetails = sessionDetailsRepository.findById(sessionId).get();

        if (SessionStatus.FINISHED == sessionDetails.getStatus()) {
            return;
        } else if (SessionStatus.ACTIVE == sessionDetails.getStatus()) {
            bookingRepository.updateBookingStatusOfSessionId(BookingStatus.EXPIRED, sessionDetails.getSessionId(), Arrays.asList(BookingStatus.BOOKED, BookingStatus.SUBMITTED));
        } else if (SessionStatus.INPROGRESS == sessionDetails.getStatus()) {
            bookingRepository.updateBookingStatusOfSessionId(BookingStatus.EXPIRED, sessionDetails.getSessionId(), Arrays.asList(BookingStatus.BOOKED));
            bookingRepository.updateBookingStatusOfSessionId(BookingStatus.CANCELLED_BY_ADMIN, sessionDetails.getSessionId(), Arrays.asList(BookingStatus.SUBMITTED));
        }

        setCompletionValueAndFinishSession(sessionDetails);
    }

    @Transactional()
    public void cancelSession(UUID sessionId) {
        SessionDetails sessionDetails = sessionDetailsRepository.findById(sessionId).get();

        if (SessionStatus.CANCELLED == sessionDetails.getStatus()) {
            return;
        } else {
            bookingRepository.updateBookingStatusOfSessionId(BookingStatus.CANCELLED_BY_ADMIN, sessionDetails.getSessionId(), Arrays.asList(BookingStatus.BOOKED,BookingStatus.SUBMITTED));
        }

        sessionDetails.setStatus(SessionStatus.CANCELLED);
        sessionDetails.setNextAvailableToken(ZERO);
        sessionDetails.setAvailableToken(ZERO);
        sessionDetailsRepository.save(sessionDetails);
    }

    @Transactional()
    public AdminSummary getActiveSession(String loginId) {
        UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
        UUID clientId = userDetails.getClientId();

        List<SessionDetails> sessionDetails = (List<SessionDetails>) sessionDetailsRepository.findByClientIdAndStatusIn(clientId, Arrays.asList(SessionStatus.INPROGRESS));

        if (sessionDetails.isEmpty()) {
            throw new AdminException("No Active session found");
        } else {
            return getAdminSummary(clientId, sessionDetails);
        }
    }

    public TokenInfo getLastToken(UUID sessionId) {
        SessionDetails sessionDetails = sessionDetailsRepository.findById(sessionId).get();
        if (SessionStatus.INPROGRESS != sessionDetails.getStatus()) {
            throw new AdminException("Session not yet started.");
        }

        BookingDetails bookingDetails = bookingService.getBookingOfLastSequenceNumber(sessionId);

        TokenInfo tokenInfo = bookingDetails == null ? null : getTokenInfo(bookingDetails, sessionDetails);

        if (tokenInfo != null) {
            tokenInfo.setHasMoreTokens(true);
        }
        return tokenInfo;
    }

    private BookingDetails getNextAvailableBooking(UUID sessionId) {
        List<BookingDetails> allSubmittedBookings = bookingService.getAllSubmittedBooking(sessionId);
        if (allSubmittedBookings.isEmpty()) {
            return null;
        }

        List<BookingDetails> onTimeSubmittedBookings = getOnTimeSubmittedBookings(allSubmittedBookings);

        if (onTimeSubmittedBookings.size() >= 1) {

            List<BookingDetails> eligibleForTurnBookings = getEligibleForTurnBooking(onTimeSubmittedBookings);
            if (eligibleForTurnBookings.size() >= 1) {

                eligibleForTurnBookings.sort(new WaitingTimeComparator());

                return eligibleForTurnBookings.iterator().next();
            } else {
                BookingDetails bookingDetailsFromWaitingList = getNextBookingFromWaitingList(allSubmittedBookings, onTimeSubmittedBookings);
                if (bookingDetailsFromWaitingList == null) {
                    List<BookingDetails> notEligibleBookingList = getNotEligibleBooingList(onTimeSubmittedBookings,eligibleForTurnBookings);
                    if (notEligibleBookingList.isEmpty()) {
                        throw new AdminException("Fatal token scheduling error");
                    }
                    notEligibleBookingList.sort(new WaitingTimeComparator());
                    return notEligibleBookingList.iterator().next();
                } else {
                    return bookingDetailsFromWaitingList;
                }
            }

        } else {
            return getNextBookingFromWaitingList(allSubmittedBookings, onTimeSubmittedBookings);
        }
    }

    private List<BookingDetails> getNotEligibleBooingList(List<BookingDetails> onTimeSubmittedBookings, List<BookingDetails> eligibleForTurnBookings) {
        return onTimeSubmittedBookings.stream().filter(booking -> !eligibleForTurnBookings.contains(booking)).collect(Collectors.toList());
    }

    private BookingDetails getNextBookingFromWaitingList(List<BookingDetails> allSubmittedBookings, List<BookingDetails> onTimeSubmittedBookings) {
        List<BookingDetails> lateSubmittedBookings = allSubmittedBookings.stream().filter(booking -> !onTimeSubmittedBookings.contains(booking)).collect(Collectors.toList());

        if (lateSubmittedBookings.isEmpty()) {
            return null;
        }

        lateSubmittedBookings.sort(new WaitingTimeComparator());

        return lateSubmittedBookings.iterator().next();
    }

    private List<BookingDetails> getEligibleForTurnBooking(List<BookingDetails> onTimeSubmittedBookings) {
        return onTimeSubmittedBookings.stream().filter(booking -> LocalTime.now().isAfter(booking.getRecommendedTime())).collect(Collectors.toList());
    }

    private List<BookingDetails> getOnTimeSubmittedBookings(List<BookingDetails> allSubmittedBookings) {
        return allSubmittedBookings.stream().filter(booking -> booking.getSubmittedDate().toLocalTime().isBefore(booking.getRecommendedTime().plusMinutes(ALLOWED_LATE_MINUTES))).collect(Collectors.toList());
    }

    private void setCompletionValueAndFinishSession(SessionDetails sessionDetails) {
        sessionDetails.setStatus(SessionStatus.FINISHED);
        sessionDetails.setNextAvailableToken(ZERO);
        sessionDetails.setAvailableToken(ZERO);
        sessionDetailsRepository.save(sessionDetails);
    }

    private boolean isClientHasOtherSessionInProgress(SessionDetails sessionDetails) {
        List<SessionDetails> result = new ArrayList<>(sessionDetailsRepository.findByClientIdAndStatusIn(sessionDetails.getClientId(), Arrays.asList(SessionStatus.INPROGRESS)));

        return !result.isEmpty();
    }

    private AdminSummary getAdminSummary(UUID clientId, List<SessionDetails> sessionDetailsList) {
        AdminSummary adminSummary = new AdminSummary();

        List<AdminSessionSummary> adminSessionSummaries = sessionDetailsList.stream().map(this::getAdminSessionSummary).collect(Collectors.toList());
        Client client = clientRepository.findById(clientId).get();
        adminSummary.setClientName(client.getClientName());
        adminSummary.setClientId(clientId);
        adminSessionSummaries.sort(new DateAndFromTimeComparatorImp());
        adminSummary.setAdminSessionSummaryList(adminSessionSummaries);

        return adminSummary;
    }

    private AdminSessionSummary getAdminSessionSummary(SessionDetails sessionDetails) {
        AdminSessionSummary adminSessionSummary = new AdminSessionSummary();
        adminSessionSummary.setAvailableTokens(sessionDetails.getAvailableToken());
        adminSessionSummary.setBookedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), BookingStatus.BOOKED).intValue());
        adminSessionSummary.setSubmittedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), BookingStatus.SUBMITTED).intValue());
        adminSessionSummary.setCompletedTokens(bookingRepository.countBySessionIdAndStatus(sessionDetails.getSessionId(), BookingStatus.COMPLETED).intValue());
        adminSessionSummary.setSessionId(sessionDetails.getSessionId());
        adminSessionSummary.setDate(sessionDetails.getDate());
        adminSessionSummary.setFromTime(sessionDetails.getFromTime());
        adminSessionSummary.setToTime(sessionDetails.getToTime());
        adminSessionSummary.setStatus(sessionDetails.getStatus());

        ClientOperation clientOperation = clientOperationRepository.findById(sessionDetails.getOperationId()).get();
        adminSessionSummary.setDay(clientOperation.getDay());

        return adminSessionSummary;
    }

    private TokenInfo getTokenInfo(BookingDetails nextBooking, SessionDetails sessionDetails) {
        UserDetails userDetails = userDetailsRepository.getOne(nextBooking.getUserId());

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setBookingId(nextBooking.getBookingId());
        tokenInfo.setTokenNumber(nextBooking.getTokenNumber());
        tokenInfo.setUserName(userDetails.getFullName());
        tokenInfo.setFromTime(sessionDetails.getFromTime());
        tokenInfo.setToTime(sessionDetails.getToTime());

        return tokenInfo;
    }

    private void setSequenceNumber(BookingDetails previousBookingDetails, BookingDetails nextBooking) {
        if (previousBookingDetails != null && nextBooking != null)
            nextBooking.setSequenceNumber(previousBookingDetails.getSequenceNumber() + 1);
        else if (nextBooking != null)
            nextBooking.setSequenceNumber(1);

        bookingRepository.save(nextBooking);
    }

    private BookingDetails completePreviousToken(UUID sessionId) {
        BookingDetails bookingDetails = bookingService.getBookingOfLastSequenceNumber(sessionId);

        if (bookingDetails != null && BookingStatus.COMPLETED != bookingDetails.getStatus()) {
            bookingDetails.setStatus(BookingStatus.COMPLETED);
            bookingDetails.setCompletedDate(LocalDateTime.now());
            bookingRepository.save(bookingDetails);
        }

        return bookingDetails;
    }

}
