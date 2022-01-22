package com.hotel.reservation.repository

import com.hotel.reservation.entity.User
import com.hotel.reservation.type.UserLoyaltyType
import com.hotel.reservation.type.UserPermissionType
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByLoginName(loginName: String): User?

    @Query("""SELECT u FROM User u
                WHERE (:userNamePattern is null OR u.name LIKE %:userNamePattern%) 
                AND (:id is null OR u.id = :id ) 
                AND (:loyalty is null OR u.loyalty = :loyalty)
                AND (:permission is null OR u.permission = :permission)""")
    fun filterUsers(
        @Param("userNamePattern") userNamePattern: String?,
        @Param("id") id: Int?,
        @Param("permission") permission: UserPermissionType?,
        @Param("loyalty") loyalty: UserLoyaltyType?,
        sort: Sort
    ): List<User>
}