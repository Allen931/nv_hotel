package com.hotel.reservation.entity

import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.UserPermissionType
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "reservations", indexes = [Index(columnList = "checkInTime, checkOutTime")])
open class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open val id: Int? = null,

    @ManyToOne
    @JoinColumn(nullable = false)
    open val user: User,

    @ManyToOne
    @JoinColumn(nullable = false)
    open var room: Room,

    @Column(nullable = false)
    open var cost: Int,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    open var checkInTime: Date,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    open var checkOutTime: Date,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    open var status: ReservationStatusType = ReservationStatusType.Pending,

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    open val payments: MutableList<Payment> = ArrayList(),
) {
    constructor(user: User, room: Room, cost: Int, checkInTime: Date, checkOutTime: Date)
            : this(null, user, room, cost, checkInTime, checkOutTime) {}
}
