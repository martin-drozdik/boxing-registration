package com.boxingregistration.app.persistence

import com.boxingregistration.app.domain.Tournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository: JpaRepository<Tournament, String>
{
    fun findByIsCurrent(current: Boolean): List<Tournament>
}