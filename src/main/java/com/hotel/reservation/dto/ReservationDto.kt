package com.hotel.reservation.dto

import com.hotel.reservation.entity.Room
import com.sun.istack.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date

data class ReservationDto(
    val room: Room?,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @field:NotNull
    val checkInTime: Date?,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @field:NotNull
    val checkOutTime: Date?
) {}