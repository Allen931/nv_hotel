package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.dto.admin.ReservationAdminDto
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
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
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.EntityManager

@RestController
@Secured
class ReservationAdminController {
    @Autowired private lateinit var reservationRepository: ReservationRepository

    @Autowired private lateinit var reservationService: ReservationService

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
        model: Model
    ): String {
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
        model.addAttribute("reservation", reservations)

        return "adminReservationList"
    }

    @PostMapping("/admin/reservation/{reservation}")
    fun editReservation(
        @PathVariable reservation: Reservation?,
        @ModelAttribute("reservationAdminDto") reservationAdminDto: ReservationAdminDto
    ): Map<String, Any> {
        if (reservation == null)
            throw IllegalArgumentException()
        try {
            reservationService.changeReservation(reservation, reservationAdminDto)
            return mapOf("success" to true)
        } catch (e: IllegalArgumentException) {
            return mapOf("success" to false, "information" to e.message.toString())
        } catch (e: DuplicateReservationException) {
            return mapOf("success" to false, "information" to "Assigned room is not available")
        }
    }
}
