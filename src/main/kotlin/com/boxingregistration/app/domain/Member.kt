package com.boxingregistration.app.domain

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Member
(
    val name: String,
    var club: String,
    val weight_category: String,
    val year_category: String,
    val n_fights: Int,
    var tournament_name: String,
    var coach: String = "",
    @Id val id: String = UUID.randomUUID().toString()
)