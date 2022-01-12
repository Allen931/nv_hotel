package com.hotel.reservation.security

import com.hotel.reservation.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class StoredUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username != null) {
            val user = userRepository.findByLoginName(username) ?: throw UsernameNotFoundException("")
            return UserPrincipal(user)
        } else {
            throw UsernameNotFoundException("")
        }
    }
}