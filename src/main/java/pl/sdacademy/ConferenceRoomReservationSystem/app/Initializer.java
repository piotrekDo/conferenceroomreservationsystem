package pl.sdacademy.ConferenceRoomReservationSystem.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoom;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoomRepository;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.Organization;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.OrganizationRepository;
import pl.sdacademy.ConferenceRoomReservationSystem.reservation.Reservation;
import pl.sdacademy.ConferenceRoomReservationSystem.reservation.ReservationRepository;

import java.time.LocalDateTime;

@Component
public class Initializer implements ApplicationRunner {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Organization organization1 = new Organization("Organization1", "some desc");
        Organization organization2 = new Organization("Organization2", "some desc");
        Organization organization3 = new Organization("Organization3", "some desc");

        ConferenceRoom conferenceRoom1 = new ConferenceRoom("room1", "IDE1", 1, true, 10, organization1);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom("room2", "IDE2", 2, true, 8, organization1);
        ConferenceRoom conferenceRoom3 = new ConferenceRoom("room3", "IDE3", 3, false, 3, organization1);

        ConferenceRoom conferenceRoom4 = new ConferenceRoom("room3", "IDE4", 3, false, 4, organization2);
        ConferenceRoom conferenceRoom5 = new ConferenceRoom("room2", "IDE5", 2, true, 8, organization2);
        ConferenceRoom conferenceRoom6 = new ConferenceRoom("room1", "IDE6", 2, false, 6, organization2);

        ConferenceRoom conferenceRoom7 = new ConferenceRoom("room3", "IDE7", 1, false, 6, organization3);
        ConferenceRoom conferenceRoom8 = new ConferenceRoom("room2", "IDE8", 2, false, 4, organization3);
        ConferenceRoom conferenceRoom9 = new ConferenceRoom("room1", "IDE9", 5, true, 9, organization3);

        Reservation booking1 = new Reservation(
                LocalDateTime.of(2022, 8, 21, 8, 0),
                LocalDateTime.of(2022, 8, 21, 10, 0),
                        "Booking1", conferenceRoom1);

        Reservation booking2 = new Reservation(
                LocalDateTime.of(2022, 8, 21, 12, 0),
                LocalDateTime.of(2022, 8, 21, 13, 0),
                "Booking2", conferenceRoom1);

        Reservation booking3 = new Reservation(
                LocalDateTime.of(2022, 8, 22, 12, 0),
                LocalDateTime.of(2022, 8, 22, 13, 0),
                "Booking3", conferenceRoom1);

        Reservation booking4 = new Reservation(
                LocalDateTime.of(2022, 8, 21, 8, 0),
                LocalDateTime.of(2022, 8, 21, 10, 0),
                "Booking4", conferenceRoom2);

        Reservation booking5 = new Reservation(
                LocalDateTime.of(2022, 8, 21, 12, 0),
                LocalDateTime.of(2022, 8, 21, 13, 0),
                "Booking5", conferenceRoom2);

        Reservation booking6 = new Reservation(
                LocalDateTime.of(2022, 8, 22, 12, 0),
                LocalDateTime.of(2022, 8, 22, 13, 0),
                "Booking6", conferenceRoom2);

        organizationRepository.save(organization1);
        organizationRepository.save(organization2);
        organizationRepository.save(organization3);

        conferenceRoomRepository.save(conferenceRoom1);
        conferenceRoomRepository.save(conferenceRoom2);
        conferenceRoomRepository.save(conferenceRoom3);
        conferenceRoomRepository.save(conferenceRoom4);
        conferenceRoomRepository.save(conferenceRoom5);
        conferenceRoomRepository.save(conferenceRoom6);
        conferenceRoomRepository.save(conferenceRoom7);
        conferenceRoomRepository.save(conferenceRoom8);
        conferenceRoomRepository.save(conferenceRoom9);

        reservationRepository.save(booking1);
        reservationRepository.save(booking2);
        reservationRepository.save(booking3);
        reservationRepository.save(booking4);
        reservationRepository.save(booking5);
        reservationRepository.save(booking6);


    }
}
