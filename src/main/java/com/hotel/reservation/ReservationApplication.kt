package com.hotel.reservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ReservationApplication

fun main(args: Array<String>) {
    runApplication<ReservationApplication>(*args)
}
