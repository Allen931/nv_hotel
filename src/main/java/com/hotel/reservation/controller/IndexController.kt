package com.hotel.reservation.controller

import com.hotel.reservation.security.UserPrincipal
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import java.security.Principal

@RestController
class IndexController {
    @GetMapping("/")
    fun index(): String {
        return "Hello World!!"
    }

    @GetMapping("/secure")
    fun testSecure(principal: Principal): String {
        return "Hello World to ${principal.name} !!"
    }
}