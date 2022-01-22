package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.admin.ReservationAdminDto
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.exception.DuplicateReservationException
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.service.ReservationService
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.annotation.Secured
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@Secured("ROLE_ADMIN")
class ReservationAdminController {
    @Autowired private lateinit var reservationRepository: ReservationRepository

    @Autowired private lateinit var reservationService: ReservationService

    @PostMapping("/admin/reservation/checkIn/{reservation}")
    fun checkIn(@PathVariable reservation: Reservation): Map<String, Boolean> {
        if (reservation.status != ReservationStatusType.Reserved) {
            throw IllegalArgumentException("Reservation status should be reserved")
        }
        reservation.status = ReservationStatusType.CheckedIn
        reservationRepository.save(reservation)
        return mapOf("success" to true)
    }

    @PostMapping("/admin/reservation/checkOut/{reservation}")
    fun checkOut(@PathVariable reservation: Reservation): Map<String, Boolean> {
        if (reservation.status != ReservationStatusType.CheckedIn) {
            throw IllegalArgumentException("Reservation status should be checked in")
        }
        reservation.status = ReservationStatusType.CheckedOut
        reservationRepository.save(reservation)
        return mapOf("success" to true)
    }

    @GetMapping("/admin/reservation")
    fun listReservations(
        @RequestParam username: String? = null,
        @RequestParam id: Int? = null,
        @RequestParam type: RoomType? = null,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkInTime: Date?,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkOutTime: Date?,
        @RequestParam status: ReservationStatusType? = null,
        @RequestParam sortBy: String? = null,
        @RequestParam order: String? = null,
        model: ModelMap
    ): ModelAndView {
        var sort: Sort = when (sortBy) {
            "id" -> Sort.by("id")
            "checkOutTime" -> Sort.by("checkOutTime")
            "type" -> Sort.by("room.type")
            "status" -> Sort.by("status")
            else -> Sort.by("checkInTime")
        }

        sort = when (order) {
            "asc" -> sort.ascending()
            else -> sort.descending()
        }

        val reservations = reservationRepository.filterReservations(
            username, id, type, checkInTime, checkOutTime, status, sort
        )

        model.addAttribute("reservations", reservations)

        return ModelAndView("admin/listReservations", model)
    }

    @RequestMapping("/admin/reservation/{reservation}")
    fun editReservation(
        @PathVariable reservation: Reservation?,
        @ModelAttribute("reservationAdminDto") reservationAdminDto: ReservationAdminDto,
        request: HttpServletRequest,
        model: ModelMap
    ): ModelAndView {
        if (reservation == null)
            throw IllegalArgumentException()
        var information = ""
        try {
            if (request.method == "POST")
            reservationService.changeReservation(reservation, reservationAdminDto)
        } catch (e: IllegalArgumentException) {
            information = e.message.toString()
        } catch (e: DuplicateReservationException) {
            information = "Assigned room is not available"
        }
        model.addAttribute("reservation", reservation)
    }
}
