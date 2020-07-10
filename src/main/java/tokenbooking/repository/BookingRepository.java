package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tokenbooking.model.BookingDetails;
import tokenbooking.model.BookingStatus;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingDetails, Long> {

    Collection<BookingDetails> findBySessionIdAndUserIdAndStatusIn(Long sessionId, Long userId, List<BookingStatus> status);

    Collection<BookingDetails> findByUserId(Long userId);

    BookingDetails findFirstBySessionIdAndSequenceNumberNotNullOrderBySequenceNumberDesc(Long sessionId);

    BookingDetails findFirstBySessionIdAndStatusOrderByTokenNumberAsc(Long sessionId, BookingStatus status);

    Long countBySessionIdAndStatus(Long sessionId,BookingStatus status);

    @Modifying
    @Query("update BookingDetails bd set bd.status= :statusToSet where bd.sessionId = :sessionId and bd.status in :statusIn")
    void updateBookingStatusOfSessionId(@Param("statusToSet") BookingStatus statusToSet, @Param("sessionId") Long sessionId, @Param("statusIn") List<BookingStatus> statusIn);
}
