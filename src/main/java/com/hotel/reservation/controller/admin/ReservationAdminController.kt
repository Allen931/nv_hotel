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
    fun checkIn(
        @PathVariable reservation: Reservation,
        model: ModelMap
    ): ModelAndView {
        if (reservation.status != ReservationStatusType.Reserved) {
            model.addAttribute("reservation", reservation)
            model.addAttribute("information", "Reservation status should be reserved")
        } else {
            reservation.status = ReservationStatusType.CheckedIn
            reservationRepository.save(reservation)
            model.addAttribute("reservation", reservation)
            model.addAttribute("information", "Check In Success")
        }
        return ModelAndView("admin/editReservation", model)
    }

    @PostMapping("/admin/reservation/checkOut/{reservation}")
    fun checkOut(
        @PathVariable reservation: Reservation,
        model: ModelMap
    ): ModelAndView {
        if (reservation.status != ReservationStatusType.CheckedIn) {
            model.addAttribute("reservation", reservation)
            model.addAttribute("information", "Reservation status should be checked in")
        } else {
            reservation.status = ReservationStatusType.CheckedOut
            reservationRepository.save(reservation)
            model.addAttribute("reservation", reservation)
            model.addAttribute("information", "Check Out Success")
        }
        return ModelAndView("admin/editReservation", model)
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
        try {
            if (request.method == "POST") {
                reservationService.changeReservation(reservation, reservationAdminDto)
                val information = "success"
                model.addAttribute("information", information)
            }
        } catch (e: IllegalArgumentException) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        } catch (e: DuplicateReservationException) {
            val information = "Assigned room is not available"
            model.addAttribute("information", information)
        }

        model.addAttribute("reservation", reservationRepository.findById(reservation.id!!).get())
        return ModelAndView("admin/editReservation", model)
    }
}
