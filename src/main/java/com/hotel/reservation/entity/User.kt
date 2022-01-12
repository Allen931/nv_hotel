package com.hotel.reservation.entity

import javax.persistence.*

@Entity
@Table(name = "users")
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    open val id: Int? = null,

    @Column(nullable = false)
    open var name: String,

    @Column(nullable = false, unique = true)
    open var loginName: String,

    @Column(nullable = false)
    open var passwordHash: String,

    @Column(nullable = false)
    open var admin: Boolean,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    open val reservations: MutableList<Reservation> = ArrayList(),
) {
    constructor(name: String, loginName: String, passwordHash: String)
            : this(null, name, loginName, passwordHash, false) {}
}
