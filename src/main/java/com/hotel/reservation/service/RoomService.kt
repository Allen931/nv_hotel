package com.hotel.reservation.service

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import com.hotel.reservation.entity.User
import com.hotel.reservation.exception.DuplicateReservationException
import com.hotel.reservation.exception.NoRoomsAvailableException
import com.hotel.reservation.exception.ReservationNotExistsException
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.ZoneId
import java.util.Date
import kotlin.random.Random

@Service
class RoomService {
    @Autowired private lateinit var roomRepository: RoomRepository
    @Autowired private lateinit var reservationRepository: ReservationRepository

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun reserveRoom(user: User, roomType: RoomType, reservationDto: ReservationDto) : Reservation {
        if (reservationDto.room != null && reservationDto.room.type != roomType) {
            throw IllegalArgumentException("Room type does not match specified room");
        }

        val checkInTime = reservationDto.checkInTime ?: throw IllegalArgumentException()
        val checkOutTime = reservationDto.checkOutTime ?: throw IllegalArgumentException()

        val room = reservationDto.room ?: assignRoom(roomType, checkInTime, checkOutTime) ?: throw NoRoomsAvailableException()

        if (reservationRepository.findByRoomAndOverlappingStayTime(room, checkInTime, checkOutTime).isNotEmpty()) {
            throw DuplicateReservationException()
        }

        val days = Duration.between(
            checkInTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
            checkOutTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()).toDays().toInt()

        val cost = days * room.costPerNight

        val reservation = Reservation(user, room, cost, checkInTime, checkOutTime)
        reservationRepository.save(reservation)

        return reservation
    }

    private fun assignRoom(roomType: RoomType, checkInTime: Date, checkOutTime: Date) : Room? {
        val rooms = roomRepository.findAvailableRoomsByTypeAndStayTime(roomType, checkInTime, checkOutTime)
        if (rooms.isEmpty()) return null

        return rooms[Random.nextInt(rooms.size)]
    }

    fun cancelReservation(reservation: Reservation) {
        if(reservation.status == ReservationStatusType.Reserved){
            reservation.status = ReservationStatusType.Cancelled
        } else {
            throw ReservationNotExistsException()
        }

    }
}