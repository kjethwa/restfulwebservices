package tokenbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tokenbooking.model.BookingDetails;
import tokenbooking.model.BookingStatus;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingDetails, UUID> {

    Collection<BookingDetails> findBySessionIdAndUserIdAndStatusIn(UUID sessionId, UUID userId, List<BookingStatus> status);

    Collection<BookingDetails> findByUserId(UUID userId);

    BookingDetails findFirstBySessionIdAndSequenceNumberNotNullOrderBySequenceNumberDesc(UUID sessionId);

    BookingDetails findFirstBySessionIdAndStatusOrderByTokenNumberAsc(UUID sessionId, BookingStatus status);

    Long countBySessionIdAndStatus(UUID sessionId,BookingStatus status);

    @Modifying
    @Query("update BookingDetails bd set bd.status= :statusToSet where bd.sessionId = :sessionId and bd.status in :statusIn")
    void updateBookingStatusOfSessionId(@Param("statusToSet") BookingStatus statusToSet, @Param("sessionId") UUID sessionId, @Param("statusIn") List<BookingStatus> statusIn);
}
