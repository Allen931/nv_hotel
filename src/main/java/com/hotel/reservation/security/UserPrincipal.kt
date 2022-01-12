package com.hotel.reservation.security

import com.hotel.reservation.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal (
    var user: User
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        if (user.admin) {
            return AuthorityUtils.createAuthorityList("USER", "ADMIN")
        } else {
            return AuthorityUtils.createAuthorityList("USER")
        }
    }

    override fun getPassword(): String = user.passwordHash
    override fun getUsername(): String = user.loginName
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
