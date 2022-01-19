package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.entity.Payment
import com.hotel.reservation.entity.Room
import com.hotel.reservation.repository.RoomRepository
import com.hotel.reservation.service.RoomService
import com.hotel.reservation.type.RoomType
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@Secured
class RoomAdminController {
    @Autowired
    private lateinit var roomRepository: RoomRepository
    @Autowired
    private lateinit var roomService: RoomService

    @GetMapping("/admin/room")
    fun listRooms(@RequestParam roomType: RoomType?, model: ModelMap): ModelAndView {
        val rooms = if (roomType == null) roomRepository.findAll() else roomRepository.findByType(roomType)
        model.addAttribute("rooms", rooms)
        return ModelAndView("admin/listRooms")
    }

    @GetMapping("/admin/room/{room}")
    fun showRoom(
        @ModelAttribute @PathVariable room: Room,
        model: ModelMap
    ): ModelAndView = ModelAndView("admin/showRoom", model)

    @PostMapping("/admin/room")
    fun createRoom(@ModelAttribute @Valid roomDto: RoomDto): Map<String, Boolean> {
        roomService.createRoom(roomDto)
        return mapOf("success" to true)
    }

    @PostMapping("/admin/room/{room}")
    fun editRoom(
        @PathVariable room: Room?,
        @ModelAttribute @Valid roomDto: RoomDto
    ): Map<String, Any> {
        try {
            roomService.editRoom(room, roomDto)
        } catch (e: IllegalArgumentException) {
            return mapOf("success" to false, "information" to e.message.toString())
        }

        return mapOf("success" to true)
    }

    @DeleteMapping("/admin/room/{room}")
    fun deleteRoom(@PathVariable room: Room): Map<String, Boolean> {
        roomRepository.delete(room)
        return mapOf("success" to true)
    }
}