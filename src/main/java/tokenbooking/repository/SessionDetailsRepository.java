package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.SessionDetails;
import tokenbooking.model.SessionStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SessionDetailsRepository extends JpaRepository<SessionDetails, UUID> {

    Collection<SessionDetails> findByClientIdAndDateBetweenAndStatusIn(UUID clientId, LocalDate startDate, LocalDate endDate, List<SessionStatus> status);

    Collection<SessionDetails> findByClientIdAndDateBetween(UUID clientId, LocalDate startDate, LocalDate endDate);

    Collection<SessionDetails> findByClientIdAndStatusIn(UUID clientId, List<SessionStatus> status);

    List<SessionDetails> findByClientIdAndDateBeforeAndStatusIn(UUID clientId, LocalDate date, List<SessionStatus> status);

}
