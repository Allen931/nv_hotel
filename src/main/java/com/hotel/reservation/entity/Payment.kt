package com.hotel.reservation.entity

import com.hotel.reservation.type.PaymentStatusType
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "payments")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Int,

    @ManyToOne
    @JoinColumn(nullable = false)
    private val reservation: Reservation,

    @Column(nullable = false)
    var amount: Int,

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private val paymentTime: Date,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatusType = PaymentStatusType.Pending,
) {}
