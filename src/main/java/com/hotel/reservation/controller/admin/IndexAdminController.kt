package com.hotel.reservation.controller.admin

import com.hotel.reservation.dto.UserDto
import com.hotel.reservation.exception.UserAlreadyExistsException
import com.hotel.reservation.security.SecurityContext
import com.hotel.reservation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@Secured("ROLE_ADMIN")
class IndexAdminController {
    @GetMapping("/admin")
    fun index(): String {
        return "admin/index"
    }
}