package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tokenbooking.admin.model.ResponseMessage;
import tokenbooking.admin.model.ResponseStatus;
import tokenbooking.model.BookingDetails;
import tokenbooking.model.BookingSummary;
import tokenbooking.model.ClientAndSessionDetails;
import tokenbooking.service.BookingService;

import java.security.Principal;
import java.util.List;

@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/booktoken", method = RequestMethod.POST)
    public ResponseMessage bookToken(@RequestBody BookingDetails bookingDetails, Principal principal) {
        try{
            BookingDetails resultBookingDetails = bookingService.bookToken(bookingDetails, principal.getName());
            return new ResponseMessage(resultBookingDetails, ResponseStatus.SUCCESS);
        } catch (Exception e) {
            return new ResponseMessage(bookingDetails, e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/users/bookings", method = RequestMethod.GET)
    public ResponseMessage<List<BookingSummary>> getAllBookingsOfUser(Principal principal) {
        try {
            List<BookingSummary> bookingSummaryList = bookingService.getAllBookingOfUser(principal.getName());
            return new ResponseMessage(bookingSummaryList, ResponseStatus.SUCCESS);
        } catch (Exception e) {
            return new ResponseMessage(null, e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/cancelBooking/{bookingId}", method = RequestMethod.PUT)
    public ResponseMessage cancelBooking(@PathVariable Long bookingId) {
        try {
            BookingSummary bookingSummary = bookingService.cancelBooking(bookingId);
            return new ResponseMessage(bookingSummary, ResponseStatus.SUCCESS);
        } catch (Exception e) {
            return new ResponseMessage(null, e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/submitBooking/{bookingId}", method = RequestMethod.PUT)
    public ResponseMessage submitBooking(@PathVariable Long bookingId) {
        try {
            BookingSummary bookingSummary = bookingService.submitBooking(bookingId);
            return new ResponseMessage(bookingSummary, ResponseStatus.SUCCESS);
        } catch (Exception e) {
            return new ResponseMessage(null, e.getMessage(), ResponseStatus.FAILURE);
        }
    }
}
