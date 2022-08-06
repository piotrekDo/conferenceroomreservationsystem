package pl.sdacademy.ConferenceRoomReservationSystem.conference_room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.Organization;
import pl.sdacademy.ConferenceRoomReservationSystem.organization.OrganizationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
class ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final OrganizationRepository organizationRepository;
    private final ConferenceRoomUpdater conferenceRoomUpdater;
    private final ConferenceRoomTransformer conferenceRoomTransformer;

    @Autowired
    ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository,
                          OrganizationRepository organizationRepository,
                          ConferenceRoomUpdater conferenceRoomUpdater,
                          ConferenceRoomTransformer conferenceRoomTransformer) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.organizationRepository = organizationRepository;
        this.conferenceRoomUpdater = conferenceRoomUpdater;
        this.conferenceRoomTransformer = conferenceRoomTransformer;
    }

    List<ConferenceRoomDto> getAllConferenceRooms() {
        return conferenceRoomRepository.findAll().stream()
                .map(conferenceRoomTransformer::toDto)
                .collect(Collectors.toList());
    }

    ConferenceRoomDto addConferenceRoom(ConferenceRoomDto conferenceRoomDto) {
        ConferenceRoom conferenceRoom = conferenceRoomTransformer.fromDto(conferenceRoomDto);
        Organization organizationFromRepo = organizationRepository.findByName(conferenceRoom.getOrganization().getName())
                        .orElseThrow(()->{
                            throw new NoSuchElementException();
                        });
        conferenceRoom.setOrganization(organizationFromRepo);
        conferenceRoomRepository.findByNameAndOrganization_Name(conferenceRoom.getName(),
                        conferenceRoom.getOrganization().getName())
                .ifPresent(cr -> {
                    throw new IllegalArgumentException("Conference room already exists!");
                });
        return conferenceRoomTransformer.toDto(conferenceRoomRepository.save(conferenceRoom));
    }

    ConferenceRoomDto deleteConferenceRoom(String id) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No conference room found!"));
        conferenceRoomRepository.deleteById(id);
        return conferenceRoomTransformer.toDto(conferenceRoom);
    }

    ConferenceRoomDto updateConferenceRoom(String id, ConferenceRoomDto conferenceRoom) {
        return conferenceRoomTransformer.toDto(
                conferenceRoomUpdater.update(id, conferenceRoomTransformer.fromDto(conferenceRoom))
        );
    }

}
