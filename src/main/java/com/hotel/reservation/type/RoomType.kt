package com.hotel.reservation.type

enum class RoomType {
    Economy, Balcony, Business, Luxury, Suite
}

val RoomTypePriceMap = mapOf(
    RoomType.Economy to 100,
    RoomType.Balcony to 110,
    RoomType.Business to 150,
    RoomType.Luxury to 200,
    RoomType.Suite to 500
)