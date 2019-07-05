package com.boxingregistration.app.persistence

import com.boxingregistration.app.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String>
{
    fun findByEmail(email: String): User
}