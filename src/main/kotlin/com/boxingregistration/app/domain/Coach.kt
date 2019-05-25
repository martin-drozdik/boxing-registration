package com.boxingregistration.app.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Coach
(
    val name: String,
    @Id val email: String,
    val password_hash: String,
    val club: String
)