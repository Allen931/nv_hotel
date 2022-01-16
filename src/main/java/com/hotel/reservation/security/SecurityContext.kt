package com.hotel.reservation.security

import com.hotel.reservation.entity.User
import com.hotel.reservation.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityContext {
    @Autowired private lateinit var userRepository: UserRepository

    val currentUser: User? get() {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is AnonymousAuthenticationToken) return null

        return userRepository.findByLoginName(auth.name)
    }
}