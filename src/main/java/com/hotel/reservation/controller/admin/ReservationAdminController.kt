package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.security.SecurityContext
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.UserPermissionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Secured
class ReservationAdminController {
    @Autowired private lateinit var reservationRepository: ReservationRepository

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
        reservation.status = ReservationStatusType.Completed
        reservationRepository.save(reservation)
        return mapOf("success" to true)
    }
}