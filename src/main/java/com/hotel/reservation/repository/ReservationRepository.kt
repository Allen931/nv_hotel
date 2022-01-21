package com.hotel.reservation.repository

import com.hotel.reservation.entity.Reservation
import com.hotel.reservation.entity.Room
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.RoomType
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface ReservationRepository : CrudRepository<Reservation, Int> {
    @Query("""SELECT r FROM Reservation r
                WHERE (:userNamePattern is null OR r.user.name LIKE %:userNamePattern%) 
                AND (:roomType is null OR r.room.type = :roomType)
                AND (:id is null OR r.id = :id )
                AND (:checkInTime is null OR r.checkInTime >= :checkInTime)
                AND (:checkOutTime is null OR r.checkOutTime <= :checkOutTime)
                AND (:reservationStatus is null OR r.status = :reservationStatus)""")
    fun filterReservations(
        @Param("userNamePattern") userNamePattern: String?,
        @Param("id") id: Int?,
        @Param("roomType") roomType: RoomType?,
        @Param("checkInTime") checkInTime: Date?,
        @Param("checkOutTime") checkOutTime: Date?,
        @Param("reservationStatus") reservationStatusType: ReservationStatusType?,
        sort: Sort
    ): List<Reservation>

    @Query("""SELECT r FROM Reservation r 
                WHERE r.room = :room 
                AND r.checkOutTime > :checkInTime 
                AND r.checkInTime < :checkOutTime 
                AND r.status <> com.hotel.reservation.type.ReservationStatusType.Cancelled
                AND r <> :ignoredReservation
                """)
    fun findByRoomAndOverlappingStayTimeIgnoringReservation(
        @Param("room") room: Room,
        @Param("checkInTime") checkInTime: Date,
        @Param("checkOutTime") checkOutTime: Date,
        @Param("ignoredReservation") ignoredReservation: Reservation?
    ): List<Reservation>
}
