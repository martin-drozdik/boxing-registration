package com.boxingregistration.app.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Member
(
    @Id val name: String,
    val coach: String,
    val club: String,
    val category: String,
    val nMatches: Int
)