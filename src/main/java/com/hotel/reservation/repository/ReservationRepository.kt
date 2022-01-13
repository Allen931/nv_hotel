package com.hotel.reservation.repository

import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface ReservationRepository : CrudRepository<Reservation, Int> {
    @Query("SELECT r FROM Reservation r WHERE r.room = :room AND r.checkOutTime > :checkInTime AND r.checkInTime < :checkOutTime AND r.status <> com.hotel.reservation.type.ReservationStatusType.Cancelled")
    fun findByRoomAndOverlappingStayTime(
        @Param("room") room: Room,
        @Param("checkInTime") checkInTime: Date,
        @Param("checkOutTime") checkOutTime: Date
    ): List<Reservation>
}
