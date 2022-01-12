package com.hotel.reservation.repository

import com.hotel.reservation.entity.Reservation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : CrudRepository<Reservation, Int> {
}