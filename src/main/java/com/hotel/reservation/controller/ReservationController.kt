package com.hotel.reservation.controller

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.entity.User
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import com.hotel.reservation.security.SecurityContext
import com.hotel.reservation.service.RoomService
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.util.Date
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
class ReservationController {
    @Autowired private lateinit var roomRepository: RoomRepository
    @Autowired private lateinit var reservationRepository: ReservationRepository
    @Autowired private lateinit var roomService: RoomService
    @Autowired private lateinit var securityContext: SecurityContext

    @GetMapping("/reservation/roomAvailability")
    fun roomAvailability(
        @RequestParam("checkInTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkInTime: Date?,
        @RequestParam("checkOutTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkOutTime: Date?,
        @RequestParam("roomType") type: RoomType?,
        model: Model
    ): String {
        if (checkInTime != null && checkOutTime != null)
            model.addAttribute("rooms", roomRepository.findAvailableRoomsByTypeAndStayTime(
                type, checkInTime, checkOutTime
            ))

        return "roomAvailability"
    }

    @RequestMapping("/reservation/{roomType}")
    @Secured
    fun reserveRoom(
        @PathVariable roomType: RoomType,
        @ModelAttribute("reservation") reservationDto: ReservationDto,
        request: HttpServletRequest
    ): String {
        if (request.method == "POST") {
            try {
                val reservation = roomService.reserveRoom(securityContext.currentUser!!, roomType, reservationDto)
            } catch (e: Exception) {
                throw e
            }
        }

        return "reserveRoom"
    }

    @GetMapping("/reservation/show")
    @Secured
    fun showReservation(
        @RequestParam("user") user: User?,
        model: Model
    ): String {
        if (user != null) {
            val currentUser = securityContext.currentUser
            if (currentUser != null && user == currentUser)
                model.addAttribute("reservations", user.reservations)
        }
        return "showReservation"
    }

}