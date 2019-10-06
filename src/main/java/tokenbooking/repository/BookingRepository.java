package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tokenbooking.model.BookingDetails;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingDetails, Long> {

    Collection<BookingDetails> findBySessionIdAndUserIdAndStatusIn(Long sessionId, Long userId, List<String> status);

    Collection<BookingDetails> findByUserId(Long userId);

    BookingDetails findFirstBySessionIdAndSequenceNumberNotNullOrderBySequenceNumberDesc(Long sessionId);

    BookingDetails findFirstBySessionIdAndStatusOrderByTokenNumberAsc(Long sessionId, String status);

    Long countBySessionIdAndStatus(Long sessionId,String status);
}
