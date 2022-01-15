package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import com.hotel.reservation.entity.User
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
import javax.persistence.criteria.Predicate

@RestController
@Secured
class ReservationAdminController {
    @Autowired private lateinit var reservationRepository: ReservationRepository
    @Autowired private lateinit var reservationService: ReservationService
    @Autowired private lateinit var entityManager: EntityManager

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
            "checkInTime" -> Sort.by("checkInTime")
            "checkOutTime" -> Sort.by("checkOutTime")
            "type" -> Sort.by("room.type")
            "status" -> Sort.by("status")
            else -> Sort.by("id")
        }

        sort = when (order) {
            "asc" -> sort.ascending()
            else -> sort.descending()
        }

        val reservations = reservationRepository.filterReservations(
            username, id, type, checkInTime, checkOutTime, status, sort)
        model.addAttribute("reservation", reservations)

        return "adminReservationList"
    }

    @PostMapping("/admin/reservation/{reservation}")
    fun editReservation(
        @PathVariable reservation: Reservation?,
        @RequestParam type: RoomType? = null,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkInTime: Date?,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkOutTime: Date?,
        @RequestParam status: ReservationStatusType? = null,
        @RequestParam cost: Int?,
        @RequestParam room: Room?
    ): Map<String, Boolean> {
        if (reservation == null)
            throw IllegalArgumentException()

        val checkIn = when (checkInTime) {
            null -> reservation.checkInTime
            else -> checkInTime
        }

        val checkOut = when (checkOutTime) {
            null -> reservation.checkOutTime
            else -> checkOutTime
        }

        if (type != null && type != reservation.room.type) {
            if (room != null) {
                if (room.type != type) {
                    throw IllegalArgumentException()
                }
                reservation.room = room
            } else {
                val temp = reservationService.assignRoom(type, checkIn, checkOut)
                if (temp != null)
                    reservation.room = temp
            }
        } else {
            if (room != null) {
                val reservations = reservationRepository.findByRoomAndOverlappingStayTime(room, checkIn, checkOut)
                if (reservations.isEmpty()) {
                    reservation.room = room
                }
            }
        }

        if (status != null)
            reservation.status = status

        if (cost != null)
            reservation.cost = cost

        reservationRepository.save(reservation)
        return mapOf("success" to true)
    }
}
