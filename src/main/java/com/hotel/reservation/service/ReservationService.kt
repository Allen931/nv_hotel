package com.hotel.reservation.service

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import com.hotel.reservation.entity.User
import com.hotel.reservation.exception.DuplicateReservationException
import com.hotel.reservation.exception.NoRoomsAvailableException
import com.hotel.reservation.exception.ReservationAlreadyCancelledException
import com.hotel.reservation.repository.ReservationRepository
import com.hotel.reservation.repository.RoomRepository
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import com.hotel.reservation.type.UserLoyaltyType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.time.Duration
import java.time.ZoneId
import java.util.Date
import javax.validation.Valid
import kotlin.random.Random

@Service
@Validated
class ReservationService {
    @Autowired private lateinit var roomRepository: RoomRepository
    @Autowired private lateinit var reservationRepository: ReservationRepository
    @Autowired private lateinit var roomService: RoomService

    @Transactional
    fun reserveRoom(user: User, roomType: RoomType, @Valid reservationDto: ReservationDto) : Reservation {
        if (reservationDto.room != null && reservationDto.room.type != roomType) {
            throw IllegalArgumentException("Room type does not match specified room");
        }

        val checkInTime = reservationDto.checkInTime ?: throw IllegalArgumentException()
        val checkOutTime = reservationDto.checkOutTime ?: throw IllegalArgumentException()

        val room = reservationDto.room ?: assignRoom(roomType, checkInTime, checkOutTime) ?: throw NoRoomsAvailableException()

        if (!roomService.isRoomAvailable(room, checkInTime, checkOutTime)) {
            throw DuplicateReservationException()
        }

        val days = Duration.between(
            checkInTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
            checkOutTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()).toDays().toInt()

        val cost = days * room.price

        val reservation = Reservation(user, room, cost, checkInTime, checkOutTime,
            if (user.loyalty >= UserLoyaltyType.Silver) ReservationStatusType.Reserved else ReservationStatusType.Pending)
        reservationRepository.save(reservation)

        return reservation
    }

    @Transactional
    fun changeRoom(reservation: Reservation, room: Room) {
        if (room.type != reservation.room.type) {
            throw IllegalArgumentException("Cannot change room across types")
        }

        if (!roomService.isRoomAvailable(room, reservation.checkInTime, reservation.checkOutTime)) {
            throw DuplicateReservationException()
        }

        reservation.room = room
        reservationRepository.save(reservation)
    }

    fun cancelReservation(reservation: Reservation) {
        if (reservation.status != ReservationStatusType.Cancelled) {
            reservation.status = ReservationStatusType.Cancelled
        } else {
            throw ReservationAlreadyCancelledException()
        }
    }

    private fun assignRoom(roomType: RoomType, checkInTime: Date, checkOutTime: Date) : Room? {
        val rooms = roomRepository.findAvailableRoomsByTypeAndStayTime(roomType, checkInTime, checkOutTime)
        if (rooms.isEmpty()) return null

        return rooms[Random.nextInt(rooms.size)]
    }

}