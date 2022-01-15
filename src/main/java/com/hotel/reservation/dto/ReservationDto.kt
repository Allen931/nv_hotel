package com.hotel.reservation.dto

import com.hotel.reservation.entity.Room
import com.hotel.reservation.type.ReservationStatusType
import javax.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date

open class ReservationDto(
    val room: Room?,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @field:NotNull
    val checkInTime: Date?,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @field:NotNull
    val checkOutTime: Date?
) {}