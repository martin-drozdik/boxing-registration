package com.boxingregistration.app.persistence

import com.boxingregistration.app.domain.RegisteredUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<RegisteredUser, String>
{
    fun findByEmail(email: String): RegisteredUser?

    fun findByClub(club: String): List<RegisteredUser>
}