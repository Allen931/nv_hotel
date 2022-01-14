package com.hotel.reservation.controller

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.exception.PaymentFailedException
import com.hotel.reservation.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class PaymentController {
    @Autowired private lateinit var paymentService: PaymentService

    @GetMapping("/payment/create/{reservation}")
    fun createPayment(
        @PathVariable reservation: Reservation,
        @RequestParam amount: Int,
        model: ModelMap
    ): ModelAndView {
        val payment = paymentService.createPayment(reservation, amount)
        model.addAttribute(payment)

        return ModelAndView("redirect:/payment/pay/{payment}", model)
    }

    @RequestMapping("/payment/pay/{payment}")
    fun pay(@PathVariable payment: Payment, @RequestParam creditCardNumber: String?, model: Model): String {
        if (creditCardNumber != null) {
            try {
                paymentService.processPayment(payment, creditCardNumber)
                return "redirect:/reservation/show"
            } catch (e: PaymentFailedException) {
                model.addAttribute("error", e.message)
            }
        }

        return "pay"
    }
}
