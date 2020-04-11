package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tokenbooking.model.BookingDetails;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingDetails, Long> {

    Collection<BookingDetails> findBySessionIdAndUserIdAndStatusIn(Long sessionId, Long userId, List<String> status);

    Collection<BookingDetails> findByUserId(Long userId);

    BookingDetails findFirstBySessionIdAndSequenceNumberNotNullOrderBySequenceNumberDesc(Long sessionId);

    BookingDetails findFirstBySessionIdAndStatusOrderByTokenNumberAsc(Long sessionId, String status);

    Long countBySessionIdAndStatus(Long sessionId,String status);

    @Modifying
    @Query("update BookingDetails bd set bd.status= :statusToSet where bd.sessionId = :sessionId and bd.status in :statusIn")
    void updateBookingStatusOfSessionId(@Param("statusToSet") String statusToSet, @Param("sessionId") Long sessionId, @Param("statusIn") List<String> statusIn);
}
