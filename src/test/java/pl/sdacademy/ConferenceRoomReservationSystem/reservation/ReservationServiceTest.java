package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
class ReservationServiceTest {

    @TestConfiguration
    static class ReservationTestConfig {

        @Bean
        ReservationService reservationService(ReservationRepository reservationRepository) {
            return new ReservationService(reservationRepository);
        }
    }

    @MockBean
    ReservationRepository reservationRepository;

    @Autowired
    ReservationService reservationService;


    @Test
    void addNewReservation_should_throw_an_exception_if_reservation_is_less_than_15_minutes() {
        //given
        Reservation booking1 = new Reservation(
                LocalDateTime.of(2022, 8, 20, 10, 0),
                LocalDateTime.of(2022, 8, 20, 10, 14),
                "Booking1", null);

        //when + then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.addNewReservation(booking1));
        assertEquals("Reservation cannot be shorter than 15 minutes", exception.getMessage());
    }

    @Test
    void addNewReservation_should_throw_an_exception_if_reservation_is_longer_than_8_hours() {
        //given
        Reservation booking1 = new Reservation(
                LocalDateTime.of(2022, 8, 20, 10, 0),
                LocalDateTime.of(2022, 8, 20, 18, 1),
                "Booking1", null);

        //when + then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.addNewReservation(booking1));
        assertEquals("Reservation cannot be longer than 8 hours", exception.getMessage());
    }

}