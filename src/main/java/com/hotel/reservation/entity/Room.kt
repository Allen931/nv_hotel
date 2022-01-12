package com.hotel.reservation.entity

import com.hotel.reservation.type.RoomType
import javax.persistence.*

@Entity
@Table(name = "rooms")
open class Room(
    @Id
    @Column(nullable = false)
    var roomNumber: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: RoomType,

    @Column(nullable = false)
    var beds: Int,

    @Column(nullable = false)
    var costPerNight: Int,

    @OneToMany(mappedBy = "room")
    var reservations: Collection<Reservation>,
) {}