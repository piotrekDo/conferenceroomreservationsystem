package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoom;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    Optional<Reservation> findByReservationStartLessThanAndReservationEndGreaterThan(
            LocalDateTime endDate,
            LocalDateTime startDate);

    Optional<Reservation> findByConferenceRoom_Id(String conferenceRoomId);
}
