package com.footballmanager.team.dto

import java.util.UUID

data class TeamInfo(
    val id: UUID,
    val name: String,
    val abbreviation: String,
    val city: String,
    val country: String,
)