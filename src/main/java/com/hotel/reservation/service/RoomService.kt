package com.hotel.reservation.service

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid

@Service
@Validated
class RoomService {
    @Autowired private lateinit var reservationRepository: ReservationRepository
    @Autowired private lateinit var roomRepository: RoomRepository

    fun isRoomAvailable(room: Room, checkInTime: Date, checkOutTime: Date, ignoredReservation: Reservation? = null) =
        reservationRepository.findByRoomAndOverlappingStayTimeIgnoringReservation(room, checkInTime, checkOutTime, ignoredReservation).isEmpty()

    @Transactional
    fun createRoom(@Valid roomDto: RoomDto): Room {
        if (roomRepository.findByRoomNumber(roomDto.roomNumber!!) != null) {
            throw IllegalArgumentException("Duplicate room number")
        }

        val room = Room(roomDto.roomNumber, roomDto.roomType!!)
        roomRepository.save(room)

        return room
    }

    @Transactional
    fun editRoom(room: Room?, @Valid roomDto: RoomDto) {
        if (room == null)
            throw IllegalArgumentException("Room does not exist")
        val tempRoom = roomRepository.findByRoomNumber(roomDto.roomNumber!!)
        if (tempRoom != null && tempRoom.roomNumber != room.roomNumber) {
            throw IllegalArgumentException("Illegal room number")
        }
        if (room.reservations.isNotEmpty()) {
            throw IllegalArgumentException("The room is reserved")
        }
        room.type = roomDto.roomType!!
        roomRepository.save(room)
    }
}