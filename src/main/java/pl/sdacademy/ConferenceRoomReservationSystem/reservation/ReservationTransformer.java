package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.springframework.stereotype.Component;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoom;

@Component
public class ReservationTransformer {

    ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getReservationStart(),
                reservation.getReservationEnd(),
                reservation.getReservationName(),
                reservation.getConferenceRoom().getId()
        );
    }

    Reservation toEntity(ReservationDto dto){
        return new Reservation(
                dto.getReservationStart(),
                dto.getReservationEnd(),
                dto.getReservationName(),
                new ConferenceRoom(dto.getConferenceRoomId())
        );
    }
}
