package com.boxingregistration.app.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Tournament
(
    @Id val name: String,
    var isCurrent: Boolean
)