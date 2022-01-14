package com.hotel.reservation.service

import com.hotel.reservation.entity.Room
import com.hotel.reservation.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoomService {
    @Autowired
    private lateinit var reservationRepository: ReservationRepository

    fun isRoomAvailable(room: Room, checkInTime: Date, checkOutTime: Date) =
        reservationRepository.findByRoomAndOverlappingStayTime(room, checkInTime, checkOutTime).isNotEmpty()
}