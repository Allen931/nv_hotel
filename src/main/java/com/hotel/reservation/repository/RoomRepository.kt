package com.hotel.reservation.repository

import com.hotel.reservation.entity.Room
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : CrudRepository<Room, Int> {
}