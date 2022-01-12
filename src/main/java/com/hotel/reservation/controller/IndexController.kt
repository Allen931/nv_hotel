package com.hotel.reservation.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping

@RestController
class IndexController {
    @GetMapping("/")
    fun index(): String {
        return "Hello World!!"
    }
}