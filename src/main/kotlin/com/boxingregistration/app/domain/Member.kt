package com.boxingregistration.app.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Member
(
    @Id val name: String,
    val club: String,
    val category: String,
    val n_fights: Int,
    val year: Int,
    val coach: String = ""
)