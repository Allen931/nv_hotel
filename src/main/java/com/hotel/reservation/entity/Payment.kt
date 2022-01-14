package com.hotel.reservation.entity

import com.hotel.reservation.type.PaymentStatusType
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "payments")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int,

    @ManyToOne
    @JoinColumn(nullable = false)
    val reservation: Reservation,

    @Column(nullable = false)
    var amount: Int,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var paymentTime: Date? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatusType = PaymentStatusType.Pending,
) {}
