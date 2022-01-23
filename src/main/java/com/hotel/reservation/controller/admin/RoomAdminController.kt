package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.entity.Room
import com.hotel.reservation.repository.RoomRepository
import com.hotel.reservation.service.RoomService
import com.hotel.reservation.type.RoomType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import kotlin.Exception

@RestController
@Secured("ROLE_ADMIN")
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


    @RequestMapping("/admin/room/{room}")
    fun editRoom(
        @PathVariable room: Room,
        @ModelAttribute @Valid roomDto: RoomDto,
        request: HttpServletRequest,
        model: ModelMap
    ): ModelAndView {
        try {
            if (request.method == "POST") {
                roomService.editRoom(room, roomDto)
                val information = "success"
                model.addAttribute("information", information)
            }
        } catch (e: IllegalArgumentException) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        } catch (e: Exception) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        }
        model.addAttribute("room", room)
        return ModelAndView("admin/editRoom", model)
    }

    @RequestMapping("/admin/room/create")
    fun createRoom(
        @ModelAttribute @Valid roomDto: RoomDto?,
        request: HttpServletRequest,
        model: ModelMap
    ): ModelAndView {
        try {
            if (request.method == "POST" && roomDto != null) {
                val room = roomService.createRoom(roomDto)
                val information = "success"
                model.addAttribute("information", information)

                model.addAttribute("room", room)
                return ModelAndView("admin/editRoom", model)
            }
        } catch (e: IllegalArgumentException) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        } catch (e: Exception) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        }
        return ModelAndView("admin/createRoom", model)
    }

    @PostMapping("/admin/room/delete/{room}")
    fun deleteRoom(
        @PathVariable room: Room,
        request: HttpServletRequest,
        model: ModelMap
    ): ModelAndView {
        try {
            roomRepository.delete(room)
            val information = "success"
            model.addAttribute("information", information)
        } catch (e: IllegalArgumentException) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        } catch (e: Exception) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        }
        val rooms = roomRepository.findAll()
        model.addAttribute("rooms", rooms)
        return ModelAndView("admin/listRooms", model)
    }
}