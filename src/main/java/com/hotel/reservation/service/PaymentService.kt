package com.hotel.reservation.service

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.exception.PaymentFailedException
import com.hotel.reservation.repository.PaymentRepository
import com.hotel.reservation.type.PaymentStatusType
import com.hotel.reservation.type.ReservationStatusType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Min

@Service
@Validated
class PaymentService {
    @Autowired private lateinit var paymentRepository: PaymentRepository

    fun createPayment(reservation: Reservation, @Min(value = 1) amount: Int) : Payment {
        if (reservation.status == ReservationStatusType.Cancelled || reservation.status == ReservationStatusType.Completed) {
            throw IllegalArgumentException("Cancelled or completed reservations cannot be paid")
        }

        val payment = Payment(reservation, amount)
        paymentRepository.save(payment)

        return payment
    }

    fun processPayment(payment: Payment, creditCardNumber: String?) {
        if (payment.status != PaymentStatusType.Pending) {
            throw IllegalArgumentException("Payment status should be pending")
        }

        // Should actually call credit card company APIs here.
        if (creditCardNumber == "114514") {
            // Simulate payment failure when card number is this string
            throw PaymentFailedException("Unable to charge from your credit card")
        }
        // As we can't charge a credit card, just assume it succeeds.

        payment.status = PaymentStatusType.Paid
        payment.paymentTime = Date()
        paymentRepository.save(payment)
    }

    fun refundPayment(payment: Payment) {
        if (payment.status != PaymentStatusType.Paid) {
            throw IllegalArgumentException("Payment status should be paid")
        }

        // Should actually call credit card company APIs here.

        payment.status = PaymentStatusType.Refunded
        paymentRepository.save(payment)
    }
}