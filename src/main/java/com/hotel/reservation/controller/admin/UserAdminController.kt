package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.dto.admin.UserAdminDto
import com.hotel.reservation.entity.Room
import com.hotel.reservation.entity.User
import com.hotel.reservation.repository.UserRepository
import com.hotel.reservation.service.UserService
import com.hotel.reservation.type.UserLoyaltyType
import com.hotel.reservation.type.UserPermissionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.security.access.annotation.Secured
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@Secured("ROLE_ADMIN")
class UserAdminController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/admin/user")
    fun listUsers(
        @RequestParam name: String? = null,
        @RequestParam id: Int? = null,
        @RequestParam permission: UserPermissionType? = null,
        @RequestParam loyalty: UserLoyaltyType? = null,
        @RequestParam sortBy: String? = null,
        @RequestParam order: String? = null,
        model: ModelMap
    ): ModelAndView {
        var sort: Sort = when (sortBy) {
            "id" -> Sort.by("id")
            "name" -> Sort.by("name")
            "permission" -> Sort.by("permission")
            "loyalty" -> Sort.by("loyalty")
            else -> Sort.by("id")
        }

        sort = when (order) {
            "asc" -> sort.ascending()
            else -> sort.descending()
        }

        val users = userRepository.filterUsers(name, id, permission, loyalty, sort)

        model.addAttribute("users", users)
        return ModelAndView("admin/listUsers")
    }


    @RequestMapping("/admin/user/{user}")
    fun editUser(
        @PathVariable user: User?,
        @ModelAttribute @Valid userAdminDto: UserAdminDto,
        request: HttpServletRequest,
        model: ModelMap
    ): ModelAndView {
        if (user == null)
            throw IllegalArgumentException()

        var newUser = user
        try {
            if (request.method == "POST") {
                userService.editUser(user, userAdminDto)
                val information = "success"
                model.addAttribute("information", information)
                newUser = userRepository.findById(user.id!!).get()
            }
        } catch (e: IllegalArgumentException) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        } catch (e: Exception) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        }
        model.addAttribute("user", newUser)
        return ModelAndView("admin/editUser", model)
    }

    @PostMapping("/admin/user/remove/{user}")
    fun removeUser(
        @PathVariable user: User,
        request: HttpServletRequest,
        model: ModelMap
    ): ModelAndView {
        try {
            user.permission = UserPermissionType.Removed
            userRepository.save(user)
            val information = "success"
            model.addAttribute("information", information)
        } catch (e: IllegalArgumentException) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        } catch (e: Exception) {
            val information = e.message.toString()
            model.addAttribute("information", information)
        }
        val users = userRepository.findAll()
        model.addAttribute("users", users)
        return ModelAndView("admin/listUsers", model)
    }
}