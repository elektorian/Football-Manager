package com.footballmanager.application.dto

import java.util.UUID

data class TeamInfoDto(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
)
