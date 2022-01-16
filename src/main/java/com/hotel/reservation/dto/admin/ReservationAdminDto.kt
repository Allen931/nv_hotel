package com.hotel.reservation.dto.admin

import com.hotel.reservation.dto.ReservationDto
import com.hotel.reservation.entity.Room
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.Column
import javax.validation.constraints.NotNull

class ReservationAdminDto(
    @field:NotNull
    override var room: Room,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @field:NotNull
    override val checkInTime: Date,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @field:NotNull
    override val checkOutTime: Date,

    @field:NotNull
    val type: RoomType,

    val status: ReservationStatusType?,

    val cost: Int?,

    var otherCharges: Int?,
) : ReservationDto(room, checkInTime, checkOutTime) {}