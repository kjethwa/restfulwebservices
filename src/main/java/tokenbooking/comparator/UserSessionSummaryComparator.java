package tokenbooking.comparator;

import tokenbooking.model.UserSessionSummary;

import java.util.Comparator;

public class UserSessionSummaryComparator implements Comparator<UserSessionSummary> {

    @Override
    public int compare(UserSessionSummary o1, UserSessionSummary o2) {
        if (o1.getDate().compareTo(o2.getDate()) == 0) {
            return o1.getFromTime().compareTo(o2.getFromTime());
        }
        return o1.getDate().compareTo(o2.getDate());
    }
}
