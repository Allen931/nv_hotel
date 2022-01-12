package com.hotel.reservation.entity;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Hotel hotel;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private int beds;

    @Column(nullable = false)
    private int costPerNight;

    @OneToOne(cascade = CascadeType.ALL)
    private Reservation reservation;

    public Room(String roomNumber, RoomType roomType, int beds, int costPerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.beds = beds;
        this.costPerNight = costPerNight;
    }

    public Room() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getCostPerNight() {
        return costPerNight;
    }

    public void setCostPerNight(int costPerNight) {
        this.costPerNight = costPerNight;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
