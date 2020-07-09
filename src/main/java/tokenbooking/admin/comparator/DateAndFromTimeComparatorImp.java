package tokenbooking.admin.comparator;

import java.util.Comparator;

public class DateAndFromTimeComparatorImp implements Comparator<DateAndFromTimeComparator> {

    @Override
    public int compare(DateAndFromTimeComparator o1, DateAndFromTimeComparator o2) {
        if (o1.getDate().compareTo(o2.getDate()) == 0) {
            return o1.getFromTime().compareTo(o2.getFromTime());
        }
        return o1.getDate().compareTo(o2.getDate());
    }
}
