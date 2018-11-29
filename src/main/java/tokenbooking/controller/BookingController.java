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

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "booktoken", method = RequestMethod.POST)
    public BookingDetails bookToken(@RequestBody BookingDetails bookingDetails) {
        try {
            return bookingService.bookToken(bookingDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingDetails;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "users/{userId}/bookings", method = RequestMethod.POST)
    public List<BookingSummary> getAllBookingsOfUser(@PathVariable Long userId) {
        return bookingService.getAllBookingOfUser(userId);
    }
}
