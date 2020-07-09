package tokenbooking.admin.comparator;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DateAndFromTimeComparator {
    LocalDate getDate();
    LocalTime getFromTime();
    LocalTime getToTime();
}
