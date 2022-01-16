package com.hotel.reservation.service

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.dto.admin.ReservationAdminDto
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
import org.modelmapper.ModelMapper
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
    @Autowired
    private lateinit var roomRepository: RoomRepository
    @Autowired
    private lateinit var reservationRepository: ReservationRepository
    @Autowired
    private lateinit var roomService: RoomService
    @Autowired
    private lateinit var paymentService: PaymentService
    @Autowired
    private lateinit var modelMapper: ModelMapper

    @Transactional
    fun reserveRoom(user: User, roomType: RoomType, @Valid reservationDto: ReservationDto): Reservation {
        if (reservationDto.room != null && reservationDto.room!!.type != roomType) {
            throw IllegalArgumentException("Room type does not match specified room");
        }

        val checkInTime = reservationDto.checkInTime ?: throw IllegalArgumentException()
        val checkOutTime = reservationDto.checkOutTime ?: throw IllegalArgumentException()
        ensureValidCheckInCheckOutTime(user, checkInTime, checkOutTime)

        val room =
            reservationDto.room ?: assignRoom(roomType, checkInTime, checkOutTime) ?: throw NoRoomsAvailableException()

        if (!roomService.isRoomAvailable(room, checkInTime, checkOutTime)) {
            throw DuplicateReservationException()
        }

        val days = Duration.between(
            checkInTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
            checkOutTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
        ).toDays().toInt()

        val cost = days * room.price

        val reservation = Reservation(
            user, room, cost, checkInTime, checkOutTime,
            if (user.loyalty >= UserLoyaltyType.Silver) ReservationStatusType.Reserved else ReservationStatusType.Pending
        )
        reservationRepository.save(reservation)

        return reservation
    }

    @Transactional
    fun changeReservation(
        reservation: Reservation,
        @Valid reservationDto: ReservationDto,
        validateStayTime: Boolean = true,
        validateRoomType: Boolean = true
    ) {
        if (reservationDto is ReservationAdminDto) {
            if (reservationDto.type != reservation.room.type) {
                if (reservationDto.room.type != reservationDto.type) {
                    val temp = assignRoom(
                        reservationDto.type,
                        reservationDto.checkInTime,
                        reservationDto.checkOutTime
                    ) ?: throw IllegalArgumentException("No available rooms")
                    reservationDto.room = temp
                }
            }
        }

        if (validateRoomType)
                if (reservationDto.room!!.type != reservation.room.type)
                    throw IllegalArgumentException("Cannot change room across types")
        if (validateStayTime) {
            if ((reservationDto.checkInTime!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay() !=
                        reservation.checkInTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            .atStartOfDay()) &&
                (reservationDto.checkOutTime!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    .atStartOfDay() !=
                        reservation.checkOutTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                            .atStartOfDay())
            ) {
                throw IllegalArgumentException("Check-in and check-out date must stay the same")
            }
            ensureValidCheckInCheckOutTime(
                reservation.user,
                reservationDto.checkInTime!!,
                reservationDto.checkOutTime!!
            )
        }

        if (!roomService.isRoomAvailable(
                reservationDto.room!!,
                reservationDto.checkInTime!!,
                reservationDto.checkOutTime!!
            )
        )
            throw DuplicateReservationException()

        modelMapper.map(reservationDto, reservation)
        reservationRepository.save(reservation)
    }

    @Transactional
    fun cancelReservation(reservation: Reservation) {
        if (reservation.status == ReservationStatusType.Cancelled) {
            throw ReservationAlreadyCancelledException()
        }

        reservation.status = ReservationStatusType.Cancelled
        reservation.payments.map { paymentService.refundPayment(it) }
    }

    fun assignRoom(roomType: RoomType, checkInTime: Date, checkOutTime: Date): Room? {
        val rooms = roomRepository.findAvailableRoomsByTypeAndStayTime(roomType, checkInTime, checkOutTime)
        if (rooms.isEmpty()) return null

        return rooms[Random.nextInt(rooms.size)]
    }

    private fun ensureValidCheckInCheckOutTime(user: User, checkInTime: Date, checkOutTime: Date) {
        val currentTime = Date()

        if (checkInTime.before(currentTime) || checkOutTime.before(currentTime))
            throw IllegalArgumentException("Check in time and check out time must be in the future")

        if (checkOutTime.before(checkInTime))
            throw IllegalArgumentException("Check out time must be later than check in time")

        val days = Duration.between(
            checkInTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
            checkOutTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay()
        ).toDays().toInt()

        if (days <= 0)
            throw IllegalArgumentException("Stay time must be more than 1 day")

        if (user.loyalty < UserLoyaltyType.Gold) {
            if (checkInTime.hours < 16) throw IllegalArgumentException("Check in time must be after 4PM")
            if (checkOutTime.hours >= 12) throw IllegalArgumentException("Check out time must be before 12PM")
        } else {
            // Gold and above members can check in early and check out late
            if (checkInTime.hours < 12) throw IllegalArgumentException("Check in time must be after 12PM")
            if (checkOutTime.hours >= 16) throw IllegalArgumentException("Check out time must be before 4PM")
        }
    }
}