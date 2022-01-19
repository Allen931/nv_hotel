package com.hotel.reservation.listener

import com.hotel.reservation.entity.Payment
import com.hotel.reservation.type.ReservationStatusType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.PostPersist
import javax.persistence.PostUpdate

@Component
class PaymentUpdateListener {
    @PostPersist
    @PostUpdate
    @Transactional
    fun onUpdate(payment: Payment) {
        // For normal members, auto update the reservation to reserved on payment.
        if (payment.reservation.status == ReservationStatusType.Pending) {
            if (payment.reservation.fullyPaid) {
                payment.reservation.status = ReservationStatusType.Reserved
            }
        }
    }
}