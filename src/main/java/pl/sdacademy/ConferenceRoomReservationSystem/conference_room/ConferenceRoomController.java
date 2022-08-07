package pl.sdacademy.ConferenceRoomReservationSystem.conference_room;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sdacademy.ConferenceRoomReservationSystem.SortType;

import java.util.List;

@RestController
@RequestMapping("/conferencerooms")
class ConferenceRoomController {

    private final ConferenceRoomService conferenceRoomService;

    ConferenceRoomController(ConferenceRoomService conferenceRoomService) {
        this.conferenceRoomService = conferenceRoomService;
    }

    @GetMapping
    List<ConferenceRoomDto> getAll(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) String organizationName,
            @RequestParam(required = false) Boolean availablity,
            @RequestParam(required = false) Integer seats,
            @RequestParam(defaultValue = "ASC") SortType sortType

    ) {
        return conferenceRoomService.getAllConferenceRooms(identifier, organizationName, availablity, seats
        ,sortType);
    }

    @PostMapping
    ConferenceRoomDto add(@Validated(AddConferenceRoom.class) @RequestBody ConferenceRoomDto conferenceRoom) {
        return conferenceRoomService.addConferenceRoom(conferenceRoom);
    }

    @DeleteMapping("/{id}")
    ConferenceRoomDto delete(@PathVariable String id){
        return conferenceRoomService.deleteConferenceRoom(id);
    }

    @PutMapping("/{id}")
    ConferenceRoomDto update(@PathVariable String id, @Validated(UpdateConferenceRoom.class) @RequestBody ConferenceRoomDto conferenceRoom) {
        return conferenceRoomService.updateConferenceRoom(id, conferenceRoom);
    }
}
