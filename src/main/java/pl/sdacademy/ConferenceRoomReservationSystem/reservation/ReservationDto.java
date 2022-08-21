package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReservationDto {
    private String id;
    @NotNull(groups = Add.class)
    @FutureOrPresent(groups = {Add.class, Update.class})
    private LocalDateTime reservationStart;
    @NotNull(groups = Add.class)
    @FutureOrPresent(groups = {Add.class, Update.class})
    private LocalDateTime reservationEnd;
    private String reservationName;
    private String conferenceRoomId;

    public ReservationDto(LocalDateTime reservationStart, LocalDateTime reservationEnd, String reservationName, String conferenceRoomId) {
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
        this.reservationName = reservationName;
        this.conferenceRoomId = conferenceRoomId;
    }

    public ReservationDto(String id, LocalDateTime reservationStart, LocalDateTime reservationEnd, String reservationName, String conferenceRoomId) {
        this.id = id;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
        this.reservationName = reservationName;
        this.conferenceRoomId = conferenceRoomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getReservationStart() {
        return reservationStart;
    }

    public void setReservationStart(LocalDateTime reservationStart) {
        this.reservationStart = reservationStart;
    }

    public LocalDateTime getReservationEnd() {
        return reservationEnd;
    }

    public void setReservationEnd(LocalDateTime reservationEnd) {
        this.reservationEnd = reservationEnd;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public String getConferenceRoomId() {
        return conferenceRoomId;
    }

    public void setConferenceRoomId(String conferenceRoomId) {
        this.conferenceRoomId = conferenceRoomId;
    }
}

interface Add {
}

interface Update {
}