package com.hotel.reservation.controller.admin

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
class IndexAdminController {
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var securityContext: SecurityContext

    @GetMapping("/admin")
    fun index(): String {
        return "admin/template"
    }

//    @GetMapping("/admin/login")
//    fun login(): String {
//        if (securityContext.currentUser != null)
//            return "redirect:/reservation"
//
//        return "admin/login"
//    }

//    @RequestMapping("/admin/register")
//    fun register(
//        @ModelAttribute("user") userDto: UserDto,
//        model: Model,
//        request: HttpServletRequest
//    ): String {
//        if (request.method == "POST") {
//            try {
//                val user = userService.register(userDto)
//                return "redirect:/login"
//            } catch (e: UserAlreadyExistsException) {
//                model.addAttribute("error", "User already exists!")
//            }
//        }
//
//        return "register"
//    }
}