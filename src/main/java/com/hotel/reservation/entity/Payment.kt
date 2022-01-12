package com.hotel.reservation.entity

import com.hotel.reservation.type.PaymentStatusType
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "payments")
open class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open val id: Int,

    @ManyToOne
    @JoinColumn(nullable = false)
    open val reservation: Reservation,

    @Column(nullable = false)
    open var amount: Int,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    open var paymentTime: Date? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    open var status: PaymentStatusType = PaymentStatusType.Pending,
) {}
