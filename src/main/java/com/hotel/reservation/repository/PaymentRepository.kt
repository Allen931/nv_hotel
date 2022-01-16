package com.hotel.reservation.repository

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.type.PaymentStatusType
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : CrudRepository<Payment, Int> {
    fun findAllByStatusAndReservation(status: PaymentStatusType, reservation: Reservation): List<Payment>
    
    fun findAllByStatus(status: PaymentStatusType): List<Payment>
    
    fun findAllByReservation(reservation: Reservation): List<Payment>
}