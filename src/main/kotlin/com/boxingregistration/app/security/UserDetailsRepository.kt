package com.boxingregistration.app.security

import com.boxingregistration.app.persistence.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class UserDetailsRepository(val userRepository: UserRepository): UserDetailsService
{
    override fun loadUserByUsername(username: String?): UserDetails
    {
        val user = userRepository.findByEmail(username!!) ?: throw UsernameNotFoundException("The user $username was not found.")
        return user
    }
}