package com.hotel.reservation.entity

import com.hotel.reservation.type.PaymentStatusType
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.UserPermissionType
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "reservations", indexes = [Index(columnList = "checkInTime, checkOutTime")])
class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(nullable = false)
    var room: Room,

    @Column(nullable = false)
    var cost: Int,

    @Column(nullable = false)
    var otherCharges: Int = 0,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var checkInTime: Date,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var checkOutTime: Date,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ReservationStatusType = ReservationStatusType.Pending,

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    val payments: MutableList<Payment> = ArrayList(),
) {
    constructor(user: User, room: Room, cost: Int, checkInTime: Date, checkOutTime: Date, status: ReservationStatusType = ReservationStatusType.Pending)
            : this(null, user, room, cost, 0, checkInTime, checkOutTime, status) {}

    val fullyPaid: Boolean get() = payments.sumOf { if (it.status == PaymentStatusType.Paid) it.amount else 0 } >= cost + otherCharges
}
