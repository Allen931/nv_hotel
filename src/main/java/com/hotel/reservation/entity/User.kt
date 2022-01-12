package com.hotel.reservation.entity

import javax.persistence.*

@Entity
@Table(name = "users")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open var id: Int? = null,

    @Column(nullable = false)
    open var name: String,

    @Column(nullable = false, unique = true)
    open var loginName: String,

    @Column(nullable = false)
    open var passwordHash: String,

    @Column(nullable = false)
    open var admin: Boolean,
) {
    constructor(name: String, loginName: String, passwordHash: String)
            : this(null, name, loginName, passwordHash, false) {}
}
