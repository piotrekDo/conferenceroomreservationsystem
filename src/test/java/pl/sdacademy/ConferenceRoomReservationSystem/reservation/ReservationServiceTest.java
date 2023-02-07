package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoom;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoomRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
class ReservationServiceTest {

    @TestConfiguration
    static class ReservationTestConfig {

        @Bean
        ReservationService reservationService(ReservationRepository reservationRepository, ConferenceRoomRepository conferenceRoomRepository, ReservationTransformer reservationTransformer) {
            return new ReservationService(reservationRepository, conferenceRoomRepository, reservationTransformer);
        }
    }

    @MockBean
    ReservationRepository reservationRepository;
    @MockBean
    ConferenceRoomRepository conferenceRoomRepository;
    @MockBean
    ReservationTransformer reservationTransformer;

    @Autowired
    ReservationService reservationService;

    @Test
    void addNewReservation_should_throw_an_exception_when_no_conference_room_found() {
        Mockito.when(reservationRepository.findByConferenceRoom_Id(null)).thenReturn(Optional.empty());

        ReservationDto booking1 = new ReservationDto(
                LocalDateTime.of(2022, 8, 20, 10, 0),
                LocalDateTime.of(2022, 8, 20, 11, 0),
                "Booking1", "213");

        //when + then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.addNewReservation(booking1));
        assertEquals("Provided conference room doesn't exist", exception.getMessage());


    }


    @Test
    void addNewReservation_should_throw_an_exception_if_reservation_is_less_than_15_minutes() {
        //given
        Mockito.when(reservationRepository.findByConferenceRoom_Id(null)).thenReturn(Optional.of(new Reservation()));
        ReservationDto booking1 = new ReservationDto(
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
        Mockito.when(reservationRepository.findByConferenceRoom_Id(null)).thenReturn(Optional.of(new Reservation()));
        ReservationDto booking1 = new ReservationDto(
                LocalDateTime.of(2022, 8, 20, 10, 0),
                LocalDateTime.of(2022, 8, 20, 18, 1),
                "Booking1", null);

        //when + then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.addNewReservation(booking1));
        assertEquals("Reservation cannot be longer than 8 hours", exception.getMessage());
    }

//    @ParameterizedTest
//    @ArgumentsSource(UpdateReservationCorrectArgumentsProvider.class)
//    void updateReservation_should_update_existing_reservation(ReservationDto updateDto) {
//        //given
//        ConferenceRoom conferenceRoom = new ConferenceRoom("dcba", "Room1", "Red", 1, true, 10, null);
//        Reservation baseReservation = new Reservation("abcd",
//                LocalDateTime.of(2022, 8, 10, 10, 0),
//                LocalDateTime.of(2022, 8, 10, 11, 0),
//                "Booking1", conferenceRoom
//        );
//        Mockito.when(reservationRepository.findById("abcd")).thenReturn(Optional.of(baseReservation));
//
//
//
//    }

}