package com.boxingregistration.app.security

import com.boxingregistration.app.persistence.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Repository

@Repository
class UserDetailsRepository(val userRepository: UserRepository): UserDetailsService
{
    override fun loadUserByUsername(username: String?): UserDetails
    {
        return userRepository.findByEmail(username!!)
    }
}