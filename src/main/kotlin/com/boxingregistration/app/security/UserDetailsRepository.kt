package com.boxingregistration.app.security

import com.boxingregistration.app.persistence.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class UserDetailsRepository(val userRepository: UserRepository): UserDetailsService
{
    override fun loadUserByUsername(username: String?): UserDetails
    {
        return userRepository.findByEmail(username!!)
    }
}