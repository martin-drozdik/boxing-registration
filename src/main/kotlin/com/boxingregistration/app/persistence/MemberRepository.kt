package com.boxingregistration.app.persistence

import com.boxingregistration.app.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<Member, String>
{
    fun findByClub(club: String): List<Member>

    fun deleteByClub(club: String): Long
}