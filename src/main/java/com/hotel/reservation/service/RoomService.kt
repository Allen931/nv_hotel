package com.hotel.reservation.service

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.entity.Room
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.validation.Valid

@Service
class RoomService {
    @Autowired
    private lateinit var reservationRepository: ReservationRepository

    fun isRoomAvailable(room: Room, checkInTime: Date, checkOutTime: Date) =
        reservationRepository.findByRoomAndOverlappingStayTime(room, checkInTime, checkOutTime).isNotEmpty()
}