package tokenbooking.admin.comparator;

import tokenbooking.model.BookingDetails;

import java.util.Comparator;

public class WaitingTimeComparator implements Comparator<BookingDetails> {
    @Override
    public int compare(BookingDetails o1, BookingDetails o2) {
        return o1.getSubmittedDate().compareTo(o2.getSubmittedDate());
    }
}
