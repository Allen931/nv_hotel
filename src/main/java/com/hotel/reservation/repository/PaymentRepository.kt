package com.hotel.reservation.repository

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.type.PaymentStatusType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : CrudRepository<Payment, Int> {

    @Query("""SELECT p FROM Payment p 
                WHERE (:status is null OR p.status = :status) 
                AND (:reservation is null OR p.reservation = :reservation) 
                """)
    fun findAllByStatusAndReservation(
        @Param("status") status: PaymentStatusType?,
        @Param("reservation") reservation: Reservation?
    ): List<Payment>
}