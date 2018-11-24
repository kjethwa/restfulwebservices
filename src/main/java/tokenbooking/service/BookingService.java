package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tokenbooking.model.BookingDetails;
import tokenbooking.model.ClientOperation;
import tokenbooking.model.SessionDetails;
import tokenbooking.repository.BookingRepository;
import tokenbooking.repository.ClientOperationRepository;
import tokenbooking.repository.SessionDetailsRepository;

import java.time.LocalDateTime;

import static tokenbooking.model.Constants.BOOKED;
import static tokenbooking.model.Constants.CREATED;
import static tokenbooking.model.Constants.ZERO;

@Service
public class BookingService {

    @Autowired
    SessionDetailsRepository sessionDetailsRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ClientOperationRepository clientOperationRepository;

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
    }

    private void updateBookingDetailsInSession(SessionDetails sessionDetails) {
        sessionDetails.setAvailableToken(sessionDetails.getAvailableToken() - 1);
        if (sessionDetails.getAvailableToken() == 0)
            sessionDetails.setNextAvailableToken(-1);
        else
            sessionDetails.setNextAvailableToken(sessionDetails.getNextAvailableToken() + 1);

        sessionDetailsRepository.save(sessionDetails);
    }
}
