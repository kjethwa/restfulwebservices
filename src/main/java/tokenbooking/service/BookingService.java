package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tokenbooking.admin.comparator.DateAndFromTimeComparatorImp;
import tokenbooking.model.*;
import tokenbooking.repository.*;
import tokenbooking.utils.HelperUtil;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    UserDetailsRepository userDetailsRepository;

    // synchronized at method is poor locking mechanism, needs improvement
    public synchronized BookingDetails bookToken(BookingDetails bookingDetails, String loginId) throws Exception {

        if (!StringUtils.isEmpty(bookingDetails)) {

            UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);
            bookingDetails.setUserId(userDetails.getUserId());

            if (StringUtils.isEmpty(bookingDetails.getSessionId()) || StringUtils.isEmpty(bookingDetails.getUserId())) {
                throw new Exception("Invalid booking reguest");
            }

            SessionDetails sessionDetails = sessionDetailsRepository.findById(bookingDetails.getSessionId()).get();
            bookingDetails.setTokenNumber(sessionDetails.getNextAvailableToken());

            //Check if the token is already booked
            validateBooking(bookingDetails, sessionDetails);

            saveTokenDetails(bookingDetails);

            updateBookingDetailsInSession(sessionDetails);
        }
        return bookingDetails;
    }

    public List<BookingSummary> getAllBookingOfUser(String loginId) {

        UserDetails userDetails = userDetailsRepository.findByLoginId(loginId);

        List<BookingSummary> bookingSummaryList = new ArrayList<>();

        List<BookingDetails> allBookings = new ArrayList<>(bookingRepository.findByUserId(userDetails.getUserId()));

        allBookings.forEach(b -> bookingSummaryList.add(this.getBookingSummary(b)));

        bookingSummaryList.sort(new DateAndFromTimeComparatorImp());
        return bookingSummaryList;
    }

    public BookingSummary cancelBooking(UUID bookingId) {
        BookingDetails bookingDetails = bookingRepository.findById(bookingId).get();
        bookingDetails.setStatus(BookingStatus.CANCELLED);
        bookingDetails.setCancelledDate(LocalDateTime.now());
        bookingRepository.save(bookingDetails);

        return getBookingSummary(bookingDetails);
    }

    @Transactional
    public BookingSummary submitBooking(UUID bookingId) throws Exception {
        BookingDetails bookingDetails = bookingRepository.findById(bookingId).get();

        if(!isSessionStarted(bookingDetails)) {
            throw new Exception("Session not yet started");
        }

        bookingDetails.setStatus(BookingStatus.SUBMITTED);
        bookingDetails.setSubmittedDate(LocalDateTime.now());
        bookingRepository.save(bookingDetails);

        return getBookingSummary(bookingDetails);
    }

    public BookingDetails getBookingOfLastSequenceNumber(UUID sessionId) {
        BookingDetails bookingDetails = bookingRepository.findFirstBySessionIdAndSequenceNumberNotNullOrderBySequenceNumberDesc(sessionId);
        return bookingDetails;
    }

    public BookingDetails getSubmittedBookingOfLeastTokenNumber(UUID sessionId) {
        BookingDetails bookingDetails = bookingRepository.findFirstBySessionIdAndStatusOrderByTokenNumberAsc(sessionId, BookingStatus.SUBMITTED);
        return bookingDetails;
    }

    private BookingSummary getBookingSummary(BookingDetails bookingDetails) {
        BookingSummary bookingSummary = new BookingSummary();
        SessionDetails sessionDetails = sessionDetailsRepository.getOne(bookingDetails.getSessionId());
        Client client = clientRepository.findById(sessionDetails.getClientId()).get();

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
        // Check is booking allowed in selected session
        if (checkIsAlreadyBookedInCurrentDay(bookingDetails, sessionDetails)) {
            throw new Exception("You can book or cancel only one token for a session");
        }
        if (checkIsAlreadyBookedInSession(bookingDetails, sessionDetails)) {
            throw new Exception("Already booked a token in the session");
        }
        if (!sessionDetails.getNextAvailableToken().equals(bookingDetails.getTokenNumber())) {
            throw new Exception("Token is already booked. Kindly book another token.");
        }
        if (bookingDetails.getTokenNumber() < sessionDetails.getNextAvailableToken()) {
            throw new Exception("Invalid Token number. Kindly book again.");
        }
        if (sessionDetails.getAvailableToken() == ZERO) {
            throw new Exception("Booking is full for the current session.");
        }
    }

    private boolean checkIsAlreadyBookedInCurrentDay(BookingDetails bookingDetails, SessionDetails sessionDetails) {
        Collection<BookingDetails> bookingDetailList = bookingRepository.findBySessionIdAndUserIdAndStatusIn(sessionDetails.getSessionId(), bookingDetails.getUserId(), Arrays.asList(BookingStatus.BOOKED, BookingStatus.SUBMITTED,BookingStatus.CANCELLED,BookingStatus.COMPLETED));
        return bookingDetailList.size() > 0;
    }

    private boolean checkIsAlreadyBookedInSession(BookingDetails bookingDetails, SessionDetails sessionDetails) {
        Collection<BookingDetails> bookingDetailList = bookingRepository.findBySessionIdAndUserIdAndStatusIn(sessionDetails.getSessionId(), bookingDetails.getUserId(), Arrays.asList(BookingStatus.BOOKED, BookingStatus.SUBMITTED));
        return bookingDetailList.size() > 0;
    }

    private void saveTokenDetails(BookingDetails bookingDetails) {
        bookingDetails.setStatus(BookingStatus.BOOKED);
        bookingDetails.setCreatedDate(LocalDateTime.now());
        bookingRepository.save(bookingDetails);
    }

    private synchronized void updateBookingDetailsInSession(SessionDetails sessionDetails) {
        sessionDetails.setAvailableToken(sessionDetails.getAvailableToken() - 1);
        if (sessionDetails.getAvailableToken() == 0)
            sessionDetails.setNextAvailableToken(-1);
        else
            sessionDetails.setNextAvailableToken(sessionDetails.getNextAvailableToken() + 1);

        sessionDetailsRepository.save(sessionDetails);
    }

    private boolean isSessionStarted(BookingDetails bookingDetails) {
        UUID sessionId = bookingDetails.getSessionId();

        SessionDetails sessionDetails = sessionDetailsRepository.findById(sessionId).get();
        return sessionDetails != null && sessionDetails.getStatus() == SessionStatus.INPROGRESS;
    }

}
