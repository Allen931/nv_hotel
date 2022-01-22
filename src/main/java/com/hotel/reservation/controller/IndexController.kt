package com.hotel.reservation.controller

import com.hotel.reservation.dto.UserDto
import com.hotel.reservation.exception.UserAlreadyExistsException
import com.hotel.reservation.security.SecurityContext
import com.hotel.reservation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class IndexController {
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var securityContext: SecurityContext

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/login")
    fun login(): String {
        if (securityContext.currentUser != null) return "redirect:/reservation"

        return "login"
    }

    @RequestMapping("/register")
    fun register(
        @ModelAttribute("user") userDto: UserDto,
        model: Model,
        request: HttpServletRequest
    ): String {
        if (securityContext.currentUser != null) return "redirect:/reservation"

        if (request.method == "POST") {
            try {
                val user = userService.register(userDto)
                return "redirect:/login"
            } catch (e: UserAlreadyExistsException) {
                model.addAttribute("error", "User already exists!")
            }
        }

        return "register"
    }
}