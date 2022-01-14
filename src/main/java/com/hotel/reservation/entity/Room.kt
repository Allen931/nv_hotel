package com.hotel.reservation.entity

import com.hotel.reservation.type.RoomType
import com.hotel.reservation.type.RoomTypePriceMap
import javax.persistence.*

@Entity
@Table(name = "rooms")
class Room(
    @Id
    @Column(nullable = false)
    var roomNumber: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: RoomType,

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    val reservations: MutableList<Reservation> = ArrayList(),
) {
    val price: Int get() = RoomTypePriceMap[type]!!
}
