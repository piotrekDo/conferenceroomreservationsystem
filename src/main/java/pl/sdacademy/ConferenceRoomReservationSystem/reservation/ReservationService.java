package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.springframework.stereotype.Service;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoom;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoomRepository;

import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service()
class ReservationService {

    private final static long RESERVATION_MIN_IN_MINUTES = 15;
    private final static long RESERVATION_MAX_IN_MINUTES = 480;


    private final ReservationRepository reservationRepository;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final ReservationTransformer reservationTransformer;

    public ReservationService(ReservationRepository reservationRepository, ConferenceRoomRepository conferenceRoomRepository, ReservationTransformer reservationTransformer) {
        this.reservationRepository = reservationRepository;
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.reservationTransformer = reservationTransformer;
    }


    ReservationDto addNewReservation(ReservationDto reservation) {

        ConferenceRoom conferenceRoom = conferenceRoomRepository.findById(reservation.getConferenceRoomId()).orElseThrow(() -> {
            throw new IllegalArgumentException("Provided conference room doesn't exist");
        });

        Reservation reservationEntity = reservationTransformer.toEntity(reservation);

        checkReservationTime(reservationEntity);
        checkReservationDuration(reservationEntity);


        reservationEntity.setConferenceRoom(conferenceRoom);
        return reservationTransformer.toDto(reservationRepository.save(reservationEntity));
    }

    ReservationDto updateReservation(ReservationDto reservation) {
        Reservation reservationById = reservationRepository.findById(reservation.getId()).orElseThrow(() ->
                new NoSuchElementException("No such reservation found"));

        boolean bookingChanged = false;

        if (reservation.getConferenceRoomId() != null && !reservation.getConferenceRoomId().equals(reservationById.getConferenceRoom().getId())) {
            ConferenceRoom updatedConferenceRoom = conferenceRoomRepository.findById(reservation.getConferenceRoomId()).orElseThrow(() ->
                    new NoSuchElementException("No such conference room found"));
            reservationById.setConferenceRoom(updatedConferenceRoom);
        }

        if (reservation.getReservationStart() != null && !reservation.getReservationStart().equals(reservationById.getReservationStart())) {
            reservationById.setReservationStart(reservation.getReservationStart());
            bookingChanged = true;
        }

        if (reservation.getReservationEnd() != null && !reservation.getReservationEnd().equals(reservationById.getReservationEnd())) {
            reservationById.setReservationEnd(reservation.getReservationEnd());
            bookingChanged = true;
        }

        if (reservation.getReservationName() != null && !reservation.getReservationName().equals(reservationById.getReservationName())) {
            reservationById.setReservationName(reservation.getReservationName());
        }

        if (bookingChanged) {
            checkReservationDuration(reservationById);
            checkReservationTime(reservationById);
        }

        return reservationTransformer.toDto(reservationRepository.save(reservationById));
    }

    private void checkReservationDuration(Reservation reservation) {
        long betweenMinutes = ChronoUnit.MINUTES.between(reservation.getReservationStart(), reservation.getReservationEnd());
        if (betweenMinutes < RESERVATION_MIN_IN_MINUTES) {
            throw new IllegalArgumentException("Reservation cannot be shorter than 15 minutes");
        }
        if (betweenMinutes > RESERVATION_MAX_IN_MINUTES) {
            throw new IllegalArgumentException("Reservation cannot be longer than 8 hours");
        }
    }

    private void checkReservationTime(Reservation reservation) {
        Optional<Reservation> existingReservation = reservationRepository.findByReservationStartLessThanAndReservationEndGreaterThan(
                reservation.getReservationEnd(), reservation.getReservationStart());
        existingReservation.ifPresent(r -> {
            throw new IllegalArgumentException("Reservation already exists! with name" + r.getReservationName());
        });
    }
}

