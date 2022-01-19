package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.repository.PaymentRepository
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.service.PaymentService
import com.hotel.reservation.type.PaymentStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
class PaymentAdminController {
    @Autowired private lateinit var paymentRepository: PaymentRepository
    @Autowired private lateinit var paymentService: PaymentService

    @PostMapping("/admin/payment/{payment}/setPaid")
    fun setPaid(@PathVariable payment: Payment): Map<String, Boolean> {
        paymentService.processPayment(payment, null)
        return mapOf("success" to true)
    }

    @PostMapping("/admin/payment/{payment}/refund")
    fun refund(@PathVariable payment: Payment): Map<String, Boolean> {
        paymentService.refundPayment(payment)
        return mapOf("success" to true)
    }

    @GetMapping("/admin/payment")
    fun listPayments(
        @RequestParam paymentStatusType: PaymentStatusType?,
        @RequestParam reservation: Reservation?,
        model: ModelMap
    ): ModelAndView {
        val payments = paymentRepository.findAllByStatusAndReservation(paymentStatusType, reservation)
        model.addAttribute("payments", payments)
        return ModelAndView("admin/listPayments")
    }

    @GetMapping("/admin/payment/{payment}")
    fun showPayment(
        @ModelAttribute @PathVariable payment: Payment,
        model: ModelMap
    ): ModelAndView = ModelAndView("admin/showPayment", model)
}