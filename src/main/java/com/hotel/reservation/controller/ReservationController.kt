package com.hotel.reservation.controller

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import com.hotel.reservation.exception.DuplicateReservationException
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import com.hotel.reservation.security.SecurityContext
import com.hotel.reservation.service.ReservationService
import com.hotel.reservation.type.RoomType
import com.hotel.reservation.type.UserLoyaltyType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.lang.Exception
import java.util.Date
import javax.servlet.http.HttpServletRequest

@Controller
class ReservationController {
    @Autowired private lateinit var roomRepository: RoomRepository
    @Autowired private lateinit var reservationRepository: ReservationRepository
    @Autowired private lateinit var reservationService: ReservationService
    @Autowired private lateinit var securityContext: SecurityContext

    @GetMapping("/reservation/roomAvailability")
    fun roomAvailability(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkInTime: Date?,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkOutTime: Date?,
        @RequestParam type: RoomType?,
        model: Model
    ): String {
        if (checkInTime != null && checkOutTime != null)
            model.addAttribute("rooms", roomRepository.findAvailableRoomsByTypeAndStayTime(
                type, checkInTime, checkOutTime))

        return "roomAvailability"
    }

    @GetMapping("/reservation")
    @Secured
    fun listReservations(model: Model): String {
        model.addAttribute("reservations", securityContext.currentUser!!.reservations)
        return "listReservations"
    }

    @GetMapping("/reservation/{reservation}")
    fun showReservation(
        @PathVariable @ModelAttribute reservation: Reservation
    ): String {
        return "showReservation"
    }

    @RequestMapping("/reservation/{reservation}/change")
    @Secured
    fun changeReservation(
        @PathVariable reservation: Reservation,
        @ModelAttribute reservationDto: ReservationDto,
        request: HttpServletRequest,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        if (request.method == "POST") {
            if (securityContext.currentUser!!.loyalty < UserLoyaltyType.Platinum &&
                reservationDto.room?.roomNumber != reservation.room.roomNumber) {
                model.addAttribute("error", "Only platinum and ambassador users can pick room")
            } else {
                try {
                    reservationService.changeReservation(reservation, reservationDto, true)

                    redirectAttributes.addAttribute("id", reservation.id)
                    return "redirect:/reservation/{id}"
                } catch (e: DuplicateReservationException) {
                    model.addAttribute("error", "The chosen check-in and check-out time is not available")
                }
            }
        }

        model.addAttribute("rooms", roomRepository.findAvailableRoomsByTypeAndStayTime(
            reservation.room.type, reservation.checkInTime, reservation.checkOutTime))

        return "changeReservation"
    }

    @GetMapping("/reservation/{reservation}/cancel")
    @Secured
    fun cancelReservation(@PathVariable reservation: Reservation): String {
        reservationService.cancelReservation(reservation)
        return "redirect:/reservation"
    }

    @RequestMapping("/reservation/reserve/{roomType}")
    @Secured
    fun reserveRoom(
        @PathVariable roomType: RoomType,
        @ModelAttribute("reservation") reservationDto: ReservationDto,
        request: HttpServletRequest,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        if (request.method == "POST") {
            try {
                val reservation = reservationService.reserveRoom(securityContext.currentUser!!, roomType, reservationDto)

                redirectAttributes.addAttribute("id", reservation.id)
                return "redirect:/reservation/{id}"
            } catch (e: Exception) {
                when (e) {
                    is IllegalArgumentException -> model.addAttribute("error", e.message)
                    is DuplicateReservationException -> model.addAttribute("error", "The time you have chosen is no longer available")
                    else -> throw e
                }
            }
        }

        return "reserveRoom"
    }
}
