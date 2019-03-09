package com.boxingregistration.app.persistence

import com.boxingregistration.app.domain.Coach
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CoachRepository: JpaRepository<Coach, String>