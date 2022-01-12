package com.hotel.reservation.entity

import com.hotel.reservation.type.RoomType
import javax.persistence.*

@Entity
@Table(name = "rooms")
open class Room(
    @Id
    @Column(nullable = false)
    open var roomNumber: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    open var type: RoomType,

    @Column(nullable = false)
    open var beds: Int,

    @Column(nullable = false)
    open var costPerNight: Int,

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    open val reservations: MutableList<Reservation> = ArrayList(),
) {}
