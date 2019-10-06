package tokenbooking.admin.service;

import Utils.HelperUtil;
import tokenbooking.admin.model.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.BookingDetails;
import tokenbooking.model.ClientOperation;
import tokenbooking.model.SessionDetails;
import tokenbooking.model.UserDetails;
import tokenbooking.repository.BookingRepository;
import tokenbooking.repository.ClientOperationRepository;
import tokenbooking.repository.SessionDetailsRepository;
import tokenbooking.repository.UserDetailsRepository;
import tokenbooking.service.BookingService;

import static tokenbooking.model.Constants.*;

@Service
public class AdminSessionService {

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


    public void startSession(Long sessionId) throws Exception {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);
        if (sessionDetails == null) {
            throw new Exception("Invalid session");
        }

        if (CREATED.equals(sessionDetails.getStatus())) {
            copyClientOperationDetails(sessionDetails);
        }

        if (!CREATED.equals(sessionDetails.getStatus()) && !ACTIVE.equals(sessionDetails.getStatus())) {
            throw new Exception("Invalid session status");
        } else {
            sessionDetails.setStatus(INPROGRESS);
            sessionDetailsRepository.save(sessionDetails);
        }
    }

    public TokenInfo getNextToken(Long sessionId) throws Exception {
        SessionDetails sessionDetails = sessionDetailsRepository.findOne(sessionId);
        if (sessionDetails == null) {
            throw new Exception("Invalid session");
        }

        if (!INPROGRESS.equals(sessionDetails.getStatus())) {
            throw new Exception("Invalid session status");
        } else {
            BookingDetails previousBookingDetails = completePreviousToken(sessionId);
            BookingDetails nextBooking = bookingService.getSubmittedBookingOfLeastTokenNumber(sessionId);

            if (nextBooking == null) {
                throw new Exception("All bookings completed");
            }

            setSequenceNumber(previousBookingDetails, nextBooking);

            return getTokenInfo(nextBooking);
        }
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

        if (bookingDetails != null) {
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
