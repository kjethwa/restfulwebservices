package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.SessionDetails;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface SessionDetailsRepository extends JpaRepository<SessionDetails, Long> {

    Collection<SessionDetails> findByClientIdAndDateBetweenAndStatusIn(Long clientId, LocalDate startDate, LocalDate endDate, List<String> status);

}
