package com.hotel.reservation.dto

import com.hotel.reservation.type.RoomType
import com.sun.istack.NotNull

data class RoomDto(
    @field:NotNull
    val roomNumber: Int? = null,

    @field:NotNull
    val roomType: RoomType? = null
) {}