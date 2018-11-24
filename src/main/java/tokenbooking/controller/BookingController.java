package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.BookingDetails;
import tokenbooking.repository.BookingRepository;
import tokenbooking.service.BookingService;

@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;

    @RequestMapping(value = "booktoken", method = RequestMethod.POST)
    public BookingDetails bookToken(@RequestBody BookingDetails bookingDetails) {
        try {
            return bookingService.bookToken(bookingDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingDetails;
    }
}
