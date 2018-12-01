package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tokenbooking.model.*;
import tokenbooking.repository.BookingRepository;
import tokenbooking.repository.ClientOperationRepository;
import tokenbooking.repository.ClientRepository;
import tokenbooking.repository.SessionDetailsRepository;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

            if (StringUtils.isEmpty(bookingDetails.getSessionId()) || StringUtils.isEmpty(bookingDetails.getUserId()) || StringUtils.isEmpty((bookingDetails.getTokenNumber()))) {
                throw new Exception("Invalid booking reguest");
            }
            SessionDetails sessionDetails = sessionDetailsRepository.findOne(bookingDetails.getSessionId());

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

        return bookingSummaryList;
    }

    public BookingSummary cancelBooking(Long bookingId) {
        BookingDetails bookingDetails = bookingRepository.findOne(bookingId);
        bookingDetails.setStatus(CANCELLED);
        bookingRepository.save(bookingDetails);

        return getBookingSummary(bookingDetails);
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

    private void saveTokenDetails(BookingDetails bookingDetails) {
        bookingDetails.setStatus(BOOKED);
        bookingDetails.setCreatedDate(LocalDateTime.now());
        bookingRepository.save(bookingDetails);
    }

    private void copyClientOperationDetails(SessionDetails sessionDetails) {
        ClientOperation clientOperation = clientOperationRepository.findOne(sessionDetails.getOperationId());
        sessionDetails.setFromTime(clientOperation.getFromTime());
        sessionDetails.setToTime(clientOperation.getToTime());
        sessionDetails.setNoOfTokens(clientOperation.getNoOfTokens());
        sessionDetails.setAvailableToken(clientOperation.getNoOfTokens());
        sessionDetails.setNextAvailableToken(START_TOKEN_NUMBER);
    }

    private void updateBookingDetailsInSession(SessionDetails sessionDetails) {
        sessionDetails.setAvailableToken(sessionDetails.getAvailableToken() - 1);
        if (sessionDetails.getAvailableToken() == 0)
            sessionDetails.setNextAvailableToken(-1);
        else
            sessionDetails.setNextAvailableToken(sessionDetails.getNextAvailableToken() + 1);

        if(sessionDetails.getStatus().equals(CREATED)){
            sessionDetails.setStatus(ACTIVE);
        }
        sessionDetailsRepository.save(sessionDetails);
    }
}
