package com.hotel.reservation.service

import com.hotel.reservation.dto.UserDto
import com.hotel.reservation.entity.User
import com.hotel.reservation.exception.UserAlreadyExistsException
import com.hotel.reservation.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var passwordEncoder: PasswordEncoder

    @Transactional
    fun register(userDto: UserDto): User {
        if (userRepository.findByLoginName(userDto.loginName) !== null) {
            throw UserAlreadyExistsException()
        }

        val user = User(
            userDto.name,
            userDto.loginName,
            passwordEncoder.encode(userDto.password)
        )

        userRepository.save(user)
        return user
    }
}
