package com.hotel.reservation.service

import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoomService {
    @Autowired private lateinit var roomRepository: RoomRepository
    @Autowired private lateinit var reservationRepository: ReservationRepository


}