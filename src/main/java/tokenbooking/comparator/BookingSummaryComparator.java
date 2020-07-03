package tokenbooking.comparator;

import tokenbooking.model.BookingSummary;

import java.util.Comparator;

public class BookingSummaryComparator implements Comparator<BookingSummary> {
    @Override
    public int compare(BookingSummary o1, BookingSummary o2) {
        if (o1.getDate().compareTo(o2.getDate()) == 0) {
            return o1.getFromTime().compareTo(o2.getFromTime());
        }
        return o1.getDate().compareTo(o2.getDate());
    }
}
