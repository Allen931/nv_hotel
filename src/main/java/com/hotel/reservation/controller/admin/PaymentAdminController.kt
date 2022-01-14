package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.repository.PaymentRepository
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.service.PaymentService
import com.hotel.reservation.type.PaymentStatusType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PaymentAdminController {
    @Autowired private lateinit var paymentRepository: PaymentRepository
    @Autowired private lateinit var paymentService: PaymentService

    @PostMapping("/admin/payment/setPaid/{payment}")
    fun setPaid(@PathVariable payment: Payment): Map<String, Boolean> {
        paymentService.processPayment(payment, null)
        return mapOf("success" to true)
    }

    @PostMapping("/admin/payment/refund/{payment}")
    fun refund(@PathVariable payment: Payment): Map<String, Boolean> {
        paymentService.refundPayment(payment)
        return mapOf("success" to true)
    }
}