package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    ReservationDto postReservation(@RequestBody @Validated(value = Add.class) ReservationDto reservationDto) {
        return reservationService.addNewReservation(reservationDto);
    }

    @PutMapping
    ReservationDto updateReservation(@RequestBody @Validated(value = Update.class) ReservationDto reservationDto) {
        return reservationService.updateReservation(reservationDto);
    }
}
