package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tokenbooking.model.BookingDetails;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingDetails,Long> {

    Collection<BookingDetails> findBySessionIdAndUserIdAndStatusIn(Long sessionId, Long userId, List<String> status);
}
