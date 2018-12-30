package com.boxingregistration.app.domain

import org.springframework.data.annotation.Id
import javax.persistence.Entity

@Entity
data class Member
(
    @Id val name: String,
    val coach: String,
    val club: String,
    val category: String,
    val nMatches: Int
)