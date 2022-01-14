package com.hotel.reservation.dto

import javax.validation.constraints.NotBlank

data class UserDto(
    @field:NotBlank
    val name: String = "",

    @field:NotBlank
    val loginName: String = "",

    @field:NotBlank
    val password: String = ""
) {}