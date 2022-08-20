package pl.sdacademy.ConferenceRoomReservationSystem.reservation;

import org.hibernate.annotations.GenericGenerator;
import pl.sdacademy.ConferenceRoomReservationSystem.conference_room.ConferenceRoom;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    @NotNull(groups = Add.class)
    @FutureOrPresent(groups = {Add.class, Update.class})
    private LocalDateTime reservationStart;
    @NotNull(groups = Add.class)
    @FutureOrPresent(groups = {Add.class, Update.class})
    private LocalDateTime reservationEnd;
    private String reservationName;
    @ManyToOne
    private ConferenceRoom conferenceRoom;

    public Reservation() {
    }

    public Reservation(String id, LocalDateTime reservationStart, LocalDateTime reservationEnd, String reservationName, ConferenceRoom conferenceRoom) {
        this(reservationStart, reservationEnd, reservationName, conferenceRoom);
        this.id = id;
    }

    public Reservation(LocalDateTime reservationStart, LocalDateTime reservationEnd, String reservationName, ConferenceRoom conferenceRoom) {
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
        this.reservationName = reservationName;
        this.conferenceRoom = conferenceRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(reservationStart, that.reservationStart) && Objects.equals(reservationEnd, that.reservationEnd) && Objects.equals(reservationName, that.reservationName) && Objects.equals(conferenceRoom, that.conferenceRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservationStart, reservationEnd, reservationName, conferenceRoom);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", reservationStart=" + reservationStart +
                ", reservationEnd=" + reservationEnd +
                ", reservationName='" + reservationName + '\'' +
                ", conferenceRoom=" + conferenceRoom +
                '}';
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

    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }
}

interface Add {
}

interface Update {
}