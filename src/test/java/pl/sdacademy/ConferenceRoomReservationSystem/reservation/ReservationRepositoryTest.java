package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    ReservationRepository reservationRepository;

    @ParameterizedTest
    @ArgumentsSource(FindByDateErrorArgumtntsProvider.class)
    void findByDate_should_return_reservation(LocalDateTime start, LocalDateTime end) {
        //given
        testEntityManager.persist(new Reservation(
                LocalDateTime.of(2022, 8, 20, 10, 0),
                LocalDateTime.of(2022, 8, 20, 11, 0),
                "Booking1", null));

        //when
        Optional<Reservation> result = reservationRepository.findByReservationStartLessThanAndReservationEndGreaterThan(
                end, start);

        //then
        assertTrue(result.isPresent());
    }

    @ParameterizedTest
    @ArgumentsSource(FindByDateOkArgumtntsProvider.class)
    void findByDate_should_not_return_reservation(LocalDateTime start, LocalDateTime end) {
        //given
        testEntityManager.persist(new Reservation(
                LocalDateTime.of(2022, 8, 20, 10, 0),
                LocalDateTime.of(2022, 8, 20, 11, 0),
                "Booking1", null));

        //when
        Optional<Reservation> result = reservationRepository.findByReservationStartLessThanAndReservationEndGreaterThan(
                end, start);

        //then
        assertTrue(result.isEmpty());
    }


}