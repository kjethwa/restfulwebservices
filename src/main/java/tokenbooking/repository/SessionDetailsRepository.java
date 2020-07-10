package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.SessionDetails;
import tokenbooking.model.SessionStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface SessionDetailsRepository extends JpaRepository<SessionDetails, Long> {

    Collection<SessionDetails> findByClientIdAndDateBetweenAndStatusIn(Long clientId, LocalDate startDate, LocalDate endDate, List<SessionStatus> status);

    Collection<SessionDetails> findByClientIdAndDateBetween(Long clientId, LocalDate startDate, LocalDate endDate);

    Collection<SessionDetails> findByClientIdAndStatusIn(Long clientId, List<SessionStatus> status);

    List<SessionDetails> findByClientIdAndDateBeforeAndStatusIn(Long clientId, LocalDate date, List<SessionStatus> status);

}
