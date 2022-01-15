package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.entity.Room
import com.hotel.reservation.repository.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.servlet.http.HttpServletRequest

@RestController
@Secured
class RoomAdminController {
    @Autowired private lateinit var roomRepository: RoomRepository

    @PostMapping("/admin/room/create")
    fun createRoom(
        @ModelAttribute("room") roomDto: RoomDto,
        request: HttpServletRequest
    ): Map<String, Boolean> {
        if (request.method == "POST") {
            try {
                val room = Room(roomDto.roomNumber!!, roomDto.roomType!!)
                roomRepository.save(room)
            } catch (e: Exception) {
                throw e
            }
        }
        return mapOf("success" to true)
    }

    @DeleteMapping("/admin/room/{roomNumber}/delete")
    fun deleteRoom(
        @PathVariable roomNumber: Int,
        request: HttpServletRequest
    ): Map<String, Boolean> {
        if (request.method == "POST") {
            try {
                val room = roomRepository.findById(roomNumber).get()
                roomRepository.delete(room)
            } catch (e: Exception) {
                throw e
            }
        }
        return mapOf("success" to true)
    }

}