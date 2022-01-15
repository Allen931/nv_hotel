package com.hotel.reservation.controller.admin

import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.User
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
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
        @RequestParam status: ReservationStatusType? = null,
        model: Model
    ): String {
        val criteriaBuilder = entityManager.criteriaBuilder

        val userQuery = criteriaBuilder.createQuery(User::class.java)
        val userRoot = userQuery.from(User::class.java)
        userQuery.select(userRoot)

        val userPredicates = mutableListOf<Predicate>()
        if (uid != null) {
            userPredicates.add(criteriaBuilder.equal(userRoot.get<Int>("id"), uid))
        }

        if (name != null) {
            userPredicates.add(criteriaBuilder.like(userRoot.get("name"), name))
        }

        if (type != null ) {
            userPredicates.add(criteriaBuilder.equal(userRoot.get<String>("roomType"), type.name))
        }

        userQuery.where(criteriaBuilder.and(*userPredicates.toTypedArray()))

        val users = entityManager.createQuery(userQuery).resultList

        val query = criteriaBuilder.createQuery(Reservation::class.java)

        val reservationRoot = query.from(Reservation::class.java)
        query.select(reservationRoot)

        val predicates = mutableListOf<Predicate>()

        if (id != null) {
            predicates.add(criteriaBuilder.equal(reservationRoot.get<Int>("id"), id))
        }

        if (checkInTime != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(reservationRoot.get<Date>("checkInTime"), checkInTime))
        }

        if (checkOutTime != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(reservationRoot.get<Date>("checkOutTime"), checkOutTime))
        }

        if (status != null) {
            predicates.add(criteriaBuilder.equal(reservationRoot.get<String>("status"), status.name))
        }

        if (users.isNotEmpty()) {
            predicates.add(reservationRoot.get<Int>("user").`in`(users))
        }

        query.where(criteriaBuilder.and(*predicates.toTypedArray()))

        val reservations = entityManager.createQuery(query).resultList

        model.addAttribute("reservations", reservations)

        return "reservationList"
    }
}
