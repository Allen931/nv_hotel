package com.hotel.reservation.dto

import com.hotel.reservation.entity.Room
import javax.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date

open class ReservationDto(
    open val room: Room?,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @field:NotNull
    open val checkInTime: Date?,

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @field:NotNull
    open val checkOutTime: Date?
) {}