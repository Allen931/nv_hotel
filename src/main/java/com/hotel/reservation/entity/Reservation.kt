package com.hotel.reservation.entity

import com.hotel.reservation.type.ReservationStatusType
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "reservations")
class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Int,

    @ManyToOne
    @JoinColumn(nullable = false)
    private val user: User,

    @ManyToOne
    @JoinColumn(nullable = false)
    private val room: Room,

    @Column(nullable = false)
    var cost: Int,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private val checkInTime: Date,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private val checkOutTime: Date,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ReservationStatusType = ReservationStatusType.Pending,

    @OneToMany(mappedBy = "reservation")
    var payments: Collection<Payment>,
) {}
