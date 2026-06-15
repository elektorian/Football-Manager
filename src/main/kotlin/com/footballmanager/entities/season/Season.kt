package com.footballmanager.entities.season

import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.entities.match.Match
import com.footballmanager.entities.season.schedule.LeagueSchedule
import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

data class Season(
    val id : UUID,
    val league: League,
    val year: Int,
    val clubs: CopyOnWriteArraySet<Club>,
    val matches: CopyOnWriteArraySet<Match>,
    var schedule: LeagueSchedule? = null,
)