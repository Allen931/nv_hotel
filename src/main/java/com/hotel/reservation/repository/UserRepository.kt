package com.hotel.reservation.repository

import com.hotel.reservation.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByLoginName(loginName: String): User?
}