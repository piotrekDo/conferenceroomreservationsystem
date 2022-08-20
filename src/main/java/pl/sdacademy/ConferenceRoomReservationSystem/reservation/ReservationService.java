package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service()
public class ReservationService {


    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    Reservation addNewReservation(Reservation reservation) {
        long betweenMinutes = ChronoUnit.MINUTES.between(reservation.getReservationStart(), reservation.getReservationEnd());
        if (betweenMinutes < TimeUnit.MINUTES.toMinutes(15)) {
            throw new IllegalArgumentException("Reservation cannot be shorter than 15 minutes");
        }
        if (betweenMinutes > TimeUnit.HOURS.toHours(8)) {
            throw new IllegalArgumentException("Reservation cannot be longer than 8 hours");
        }


        Optional<Reservation> existingReservation = reservationRepository.findByReservationStartLessThanAndReservationEndGreaterThan(
                reservation.getReservationEnd(), reservation.getReservationStart());
        existingReservation.ifPresent(r -> {
            throw new IllegalArgumentException("Reservation already exists! with name" + r.getReservationName());
        });

        return reservationRepository.save(reservation);
    }
}

