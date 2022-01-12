package com.hotel.reservation.repository

import com.hotel.reservation.entity.Payment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : CrudRepository<Payment, Int> {
}