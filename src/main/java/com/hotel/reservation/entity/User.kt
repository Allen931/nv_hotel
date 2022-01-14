package com.hotel.reservation.entity

import com.hotel.reservation.type.UserLoyaltyType
import com.hotel.reservation.type.UserPermissionType
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var loginName: String,

    @Column(nullable = false)
    var passwordHash: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var permission: UserPermissionType = UserPermissionType.Customer,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var loyalty: UserLoyaltyType = UserLoyaltyType.Member,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val reservations: MutableList<Reservation> = ArrayList(),
) {
    constructor(name: String, loginName: String, passwordHash: String, permission: UserPermissionType = UserPermissionType.Customer)
            : this(null, name, loginName, passwordHash, permission) {}
}
