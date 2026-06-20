package com.footballmanager.domain.model

import java.util.UUID

data class Round(
    val matches: List<UUID>,
    val number: Int,
    @Volatile
    var passed: Boolean,
    val seasonId: UUID,
    val id: UUID,
)
