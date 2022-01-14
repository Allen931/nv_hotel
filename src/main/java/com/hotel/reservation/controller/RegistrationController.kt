package com.hotel.reservation.controller

import com.hotel.reservation.dto.UserDto
import com.hotel.reservation.exception.UserAlreadyExistsException
import com.hotel.reservation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid
import javax.servlet.http.HttpServletRequest

@Controller
class RegistrationController {
    @Autowired
    private lateinit var userService: UserService

    @RequestMapping("/register")
    fun register(
        @ModelAttribute("user") userDto: UserDto,
        model: ModelMap,
        request: HttpServletRequest
    ): ModelAndView {
        if (request.method == "POST") {
            try {
                val user = userService.register(userDto)
                return ModelAndView("redirect:/secure", model)
            } catch (e: UserAlreadyExistsException) {
                model.addAttribute("error", "User already exists!")
            }
        }

        return ModelAndView("register", model)
    }
}