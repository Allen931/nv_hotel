package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.User
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.criteria.Predicate
import kotlin.collections.ArrayList

@RestController
@Secured
class ReservationAdminController {
    @Autowired private lateinit var reservationRepository: ReservationRepository
    @Autowired private lateinit var entityManager: EntityManager

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

    @GetMapping("/admin/reservation/")
    fun listAction(
        @RequestParam id: Int? = null,
        @RequestParam uid: Int? = null,
        @RequestParam name: String? = null,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkInTime: Date?,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") checkOutTime: Date?,
        @RequestParam type: RoomType? = null,
        @RequestParam status: ReservationStatusType? = null
    ) {
        val criteriaBuilder = entityManager.criteriaBuilder
        val query = criteriaBuilder.createQuery(Reservation::class.java)

        val reservations = query.from(Reservation::class.java)
        query.select(reservations)

        val predicates = mutableListOf<Predicate>()

        val userQuery = criteriaBuilder.createQuery(User::class.java)
        val users = userQuery.from(User::class.java)
        userQuery.select(users)

        val userPredicates = ArrayList<Predicate>()
        if (uid != null) {
            userPredicates.add(criteriaBuilder.equal(users.get<Int>("id"), uid))
        }

        if (name != null) {
            userPredicates.add(criteriaBuilder.like(users.get("name"), name))
        }

        userQuery.where()

        if (id != null) {
            predicates.add(criteriaBuilder.equal(reservations.get<Int>("id"), id))
        }
    }
}
