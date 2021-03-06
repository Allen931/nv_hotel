package com.hotel.reservation.entity

import com.hotel.reservation.listener.PaymentUpdateListener
import com.hotel.reservation.type.PaymentStatusType
import java.util.Date
import javax.persistence.*

@Entity
@EntityListeners(PaymentUpdateListener::class)
@Table(name = "payments")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(nullable = false)
    val reservation: Reservation,

    @Column(nullable = false)
    var amount: Int,

    @Temporal(TemporalType.TIMESTAMP)
    var paymentTime: Date? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatusType = PaymentStatusType.Pending,
) {
    constructor(reservation: Reservation, amount: Int): this(null, reservation, amount) {}
}
