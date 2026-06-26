package com.footballmanager.engine

import java.time.LocalDate

data class MatchPlayedEvent(
    override val date: LocalDate,
    val fixtureId: Long,
    val homeClubId: Long,
    val awayClubId: Long,
    val homeGoals: Int,
    val awayGoals: Int
) : GameEvent
