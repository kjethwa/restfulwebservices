package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.BookingDetails;
import tokenbooking.model.BookingSummary;
import tokenbooking.model.ClientAndSessionDetails;
import tokenbooking.service.BookingService;

import java.util.List;

@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/booktoken", method = RequestMethod.POST)
    public BookingDetails bookToken(@RequestBody BookingDetails bookingDetails) {
        try {
            return bookingService.bookToken(bookingDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingDetails;
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/users/{userId}/bookings", method = RequestMethod.GET)
    public List<BookingSummary> getAllBookingsOfUser(@PathVariable Long userId) {
        return bookingService.getAllBookingOfUser(userId);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/cancelBooking/{bookingId}", method = RequestMethod.PUT)
    public BookingSummary cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/submitBooking/{bookingId}", method = RequestMethod.PUT)
    public BookingSummary submitBooking(@PathVariable Long bookingId) {
        return bookingService.submitBooking(bookingId);
    }
}
