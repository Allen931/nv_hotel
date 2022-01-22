package com.hotel.reservation.repository

import com.hotel.reservation.entity.Room
import com.hotel.reservation.type.RoomType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface RoomRepository : CrudRepository<Room, Int> {
    fun findByRoomNumber(roomNumber: Int): Room?
    fun findByType(roomType: RoomType): List<Room>

    @Query("""SELECT r2 FROM Room r2 
                WHERE (:type is null OR r2.type = :type) 
                AND NOT EXISTS 
                    (SELECT r FROM Reservation r 
                        WHERE r.room = r2 
                        AND r.checkOutTime > :checkInTime 
                        AND r.checkInTime < :checkOutTime
                        AND r.status <> com.hotel.reservation.type.ReservationStatusType.Cancelled
                    )""")
    fun findAvailableRoomsByTypeAndStayTime(
        @Param("type") type: RoomType?,
        @Param("checkInTime") checkInTime: Date,
        @Param("checkOutTime") checkOutTime: Date
    ): List<Room>
}
