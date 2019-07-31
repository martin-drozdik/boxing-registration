package com.boxingregistration.app.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class RegisteredUser
(
    val fullName: String,
    @Id val email: String,
    val password_hash: String,
    val club: String,
    val isAdmin: Boolean

): UserDetails
{
    override fun getUsername() = email
    override fun getPassword() = password_hash

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}