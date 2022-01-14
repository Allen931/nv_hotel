package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.repository.PaymentRepository
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.type.PaymentStatusType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PaymentAdminController {
    @Autowired private lateinit var paymentRepository: PaymentRepository

    @PostMapping("/admin/payment/pay/{payment}")
    fun pay(
        @PathVariable payment: Payment,
        @RequestParam("creditCardNumber") creditCardNumber: String
    ): Map<String, Boolean> {
        if (payment.status != PaymentStatusType.Pending) {
            throw IllegalArgumentException("Payment status should be pending")
        }
        // TODO
        payment.status = PaymentStatusType.Paid
        paymentRepository.save(payment)
        return mapOf("success" to true)
    }

    @PostMapping("/admin/payment/refund/{payment}")
    fun refund(
        @PathVariable payment: Payment,
        @RequestParam("creditCardNumber") creditCardNumber: String
    ): Map<String, Boolean> {
        if (payment.status != PaymentStatusType.Paid) {
            throw IllegalArgumentException("Payment status should be paid")
        }
        // TODO
        payment.status = PaymentStatusType.Refunded
        paymentRepository.save(payment)
        return mapOf("success" to true)
    }
}