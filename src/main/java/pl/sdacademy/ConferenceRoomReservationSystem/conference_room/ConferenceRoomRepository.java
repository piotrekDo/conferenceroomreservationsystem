package pl.sdacademy.ConferenceRoomReservationSystem.conference_room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, String> {

    Optional<ConferenceRoom> findByName(String name);

    Optional<ConferenceRoom> findByNameAndOrganization_Name(String name, String organizationName);
}
