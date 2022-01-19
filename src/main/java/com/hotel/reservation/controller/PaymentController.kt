package com.hotel.reservation.controller

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.exception.PaymentFailedException
import com.hotel.reservation.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class PaymentController {
    @Autowired private lateinit var paymentService: PaymentService

    @GetMapping("/payment/create/{reservation}")
    fun createPayment(
        @PathVariable reservation: Reservation,
        @RequestParam amount: Int,
        redirectAttributes: RedirectAttributes
    ): String {
        val payment = paymentService.createPayment(reservation, amount)

        redirectAttributes.addAttribute("id", payment.id)
        return "redirect:/payment/{id}/pay"
    }

    @RequestMapping("/payment/{payment}/pay")
    fun pay(
        @ModelAttribute @PathVariable payment: Payment,
        @RequestParam creditCardNumber: String?,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        if (creditCardNumber != null) {
            try {
                paymentService.processPayment(payment, creditCardNumber)

                redirectAttributes.addAttribute("id", payment.reservation.id)
                return "redirect:/reservation/{id}"
            } catch (e: PaymentFailedException) {
                model.addAttribute("error", e.message)
            }
        }

        return "pay"
    }
}
