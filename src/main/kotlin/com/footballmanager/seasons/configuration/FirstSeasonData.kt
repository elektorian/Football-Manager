package com.footballmanager.seasons.configuration

import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

data class FirstSeasonData(
    val id : UUID,
    val league: UUID,
    val teams: CopyOnWriteArraySet<UUID>,
)