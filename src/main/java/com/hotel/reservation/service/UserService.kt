package com.hotel.reservation.service

import com.hotel.reservation.dto.RoomDto
import com.hotel.reservation.dto.UserDto
import com.hotel.reservation.dto.admin.UserAdminDto
import com.hotel.reservation.entity.Room
import com.hotel.reservation.entity.User
import com.hotel.reservation.exception.UserAlreadyExistsException
import com.hotel.reservation.repository.UserRepository
import com.hotel.reservation.type.ReservationStatusType
import com.hotel.reservation.type.UserLoyaltyType
import com.hotel.reservation.type.UserPermissionType
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid

@Service
@Validated
class UserService {
    @Autowired private lateinit var userRepository: UserRepository

    @Autowired private lateinit var passwordEncoder: PasswordEncoder

    @Autowired private lateinit var modelMapper: ModelMapper

    @Transactional
    fun register(@Valid userDto: UserDto): User {
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

    @Transactional
    fun editUser(
        user: User?,
        @Valid userAdminDto: UserAdminDto
    ) {
        if (user == null)
            throw IllegalArgumentException("User does not exist")
        modelMapper.map(userAdminDto, user)
        userRepository.save(user)
    }
}
