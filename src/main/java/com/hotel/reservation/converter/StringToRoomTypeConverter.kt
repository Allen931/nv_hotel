package com.hotel.reservation.converter

import com.hotel.reservation.type.RoomType
import org.springframework.core.convert.converter.Converter
import java.util.Locale
import java.lang.IllegalArgumentException

class StringToRoomTypeConverter : Converter<String, RoomType?> {
    override fun convert(source: String): RoomType? {
        return try {
            RoomType.valueOf(source)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}