package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tokenbooking.comparator.BookingSummaryComparator;
import tokenbooking.model.*;
import tokenbooking.repository.BookingRepository;
import tokenbooking.repository.ClientOperationRepository;
import tokenbooking.repository.ClientRepository;
import tokenbooking.repository.SessionDetailsRepository;
import tokenbooking.utils.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static tokenbooking.model.Constants.*;

@Service
public class BookingService {

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ClientOperationRepository clientOperationRepository;

    @Autowired
    ClientRepository clientRepository;

    public synchronized BookingDetails bookToken(BookingDetails bookingDetails) throws Exception {

        if (!StringUtils.isEmpty(bookingDetails)) {

            if (StringUtils.isEmpty(bookingDetails.getSessionId()) || StringUtils.isEmpty(bookingDetails.getUserId())) {
                throw new Exception("Invalid booking reguest");
            }

            SessionDetails sessionDetails = sessionDetailsRepository.findOne(bookingDetails.getSessionId());
            bookingDetails.setTokenNumber(sessionDetails.getNextAvailableToken());

            //Check if the token is already booked
            validateBooking(bookingDetails, sessionDetails);

            saveTokenDetails(bookingDetails);

            if (CREATED.equals(sessionDetails.getStatus())) {
                copyClientOperationDetails(sessionDetails);
            }

            updateBookingDetailsInSession(sessionDetails);
        }
        return bookingDetails;
    }

    public List<BookingSummary> getAllBookingOfUser(Long userId) {
        List<BookingSummary> bookingSummaryList = new ArrayList<>();

        List<BookingDetails> allBookings = new ArrayList<>(bookingRepository.findByUserId(userId));

        allBookings.forEach(b -> bookingSummaryList.add(this.getBookingSummary(b)));

        bookingSummaryList.sort(new BookingSummaryComparator());
        return bookingSummaryList;
    }

    public BookingSummary cancelBooking(Long bookingId) {
        BookingDetails bookingDetails = bookingRepository.findOne(bookingId);
        bookingDetails.setStatus(CANCELLED);
        bookingRepository.save(bookingDetails);

        return getBookingSummary(bookingDetails);
    }

    public BookingSummary submitBooking(Long bookingId) {
        BookingDetails bookingDetails = bookingRepository.findOne(bookingId);
        bookingDetails.setStatus(SUBMITTED);
        bookingRepository.save(bookingDetails);

        return getBookingSummary(bookingDetails);
    }

    public BookingDetails getBookingOfLastSequenceNumber(Long sessionId) {
        BookingDetails bookingDetails = bookingRepository.findFirstBySessionIdAndSequenceNumberNotNullOrderBySequenceNumberDesc(sessionId);
        return bookingDetails;
    }

    public BookingDetails getSubmittedBookingOfLeastTokenNumber(Long sessionId) {
        BookingDetails bookingDetails = bookingRepository.findFirstBySessionIdAndStatusOrderByTokenNumberAsc(sessionId, SUBMITTED);
        return bookingDetails;
    }

    private BookingSummary getBookingSummary(BookingDetails bookingDetails) {
        BookingSummary bookingSummary = new BookingSummary();
        SessionDetails sessionDetails = sessionDetailsRepository.getOne(bookingDetails.getSessionId());
        Client client = clientRepository.findOne(sessionDetails.getClientId());

        bookingSummary.setClientId(client.getClientId());
        bookingSummary.setClientName(client.getClientName());
        bookingSummary.setDate(sessionDetails.getDate());
        bookingSummary.setFromTime(sessionDetails.getFromTime());
        bookingSummary.setToTime(sessionDetails.getToTime());
        bookingSummary.setStatus(bookingDetails.getStatus());
        bookingSummary.setTokenNumber(bookingDetails.getTokenNumber());
        bookingSummary.setBookingId(bookingDetails.getBookingId());

        return bookingSummary;
    }

    private void validateBooking(BookingDetails bookingDetails, SessionDetails sessionDetails) throws Exception {
        if (!CREATED.equals(sessionDetails.getStatus())) {
            if(checkIsAlreadyBookedInSession(bookingDetails,sessionDetails)) {
                throw new Exception("Already booked a token in the session");
            }
            if (!sessionDetails.getNextAvailableToken().equals(bookingDetails.getTokenNumber())) {
                throw new Exception("Token is already booked. Kindly book another token.");
            }
            if (bookingDetails.getTokenNumber() < sessionDetails.getNextAvailableToken()) {
                throw new Exception("In valid Token number. Kindly book again.");
            }
            if (sessionDetails.getAvailableToken() == ZERO) {
                throw new Exception("Booking is full for the current session.");
            }
        }
    }

    private boolean checkIsAlreadyBookedInSession(BookingDetails bookingDetails, SessionDetails sessionDetails) {
        Collection<BookingDetails> bookingDetailList = bookingRepository.findBySessionIdAndUserIdAndStatusIn(sessionDetails.getSessionId(), bookingDetails.getUserId(), Arrays.asList(BOOKED, SUBMITTED));
        return bookingDetailList.size() > 0;
    }

    private void saveTokenDetails(BookingDetails bookingDetails) {
        bookingDetails.setStatus(BOOKED);
        bookingDetails.setCreatedDate(LocalDateTime.now());
        bookingRepository.save(bookingDetails);
    }

    private void copyClientOperationDetails(SessionDetails sessionDetails) {
        ClientOperation clientOperation = clientOperationRepository.findOne(sessionDetails.getOperationId());
        HelperUtil.copyClientOperationDetails(clientOperation, sessionDetails);
    }

    private void updateBookingDetailsInSession(SessionDetails sessionDetails) {
        sessionDetails.setAvailableToken(sessionDetails.getAvailableToken() - 1);
        if (sessionDetails.getAvailableToken() == 0)
            sessionDetails.setNextAvailableToken(-1);
        else
            sessionDetails.setNextAvailableToken(sessionDetails.getNextAvailableToken() + 1);

        if (sessionDetails.getStatus().equals(CREATED)) {
            sessionDetails.setStatus(ACTIVE);
        }
        sessionDetailsRepository.save(sessionDetails);
    }
}
