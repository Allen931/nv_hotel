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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
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

    @RequestMapping("/reservation/reserve/{roomType}")
    @Secured
    fun reserveRoom(
        @PathVariable roomType: RoomType,
        @ModelAttribute("reservation") reservationDto: ReservationDto,
        request: HttpServletRequest
    ): String {
        if (request.method == "POST") {
            try {
                val reservation = reservationService.reserveRoom(securityContext.currentUser!!, roomType, reservationDto)
            } catch (e: Exception) {
                throw e
            }
        }

        return "reserveRoom"
    }

    @GetMapping("/reservation/show")
    @Secured
    fun showReservations(model: Model): String {
        model.addAttribute("reservations", securityContext.currentUser!!.reservations)
        return "showReservations"
    }

    @RequestMapping("/reservation/changeRoom/{reservation}")
    @Secured
    fun changeRoom(@PathVariable reservation: Reservation, @RequestParam room: Room?, model: Model): String {
        if (room != null) {
            try {
                reservationService.changeRoom(reservation, room)
                return "redirect:/reservation/show"
            } catch (e: DuplicateReservationException) {
                model.addAttribute("error", "The room was already taken")
            }
        }

        model.addAttribute("rooms", roomRepository.findAvailableRoomsByTypeAndStayTime(
            reservation.room.type, reservation.checkInTime, reservation.checkOutTime))

        return "changeRoom"
    }

    @GetMapping("/reservation/cancel/{reservation}")
    @Secured
    fun cancelReservation(@PathVariable reservation: Reservation): String {
        reservationService.cancelReservation(reservation)
        return "redirect:/reservation/show"
    }
}