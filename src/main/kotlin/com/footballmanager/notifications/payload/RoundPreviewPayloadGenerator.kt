package com.footballmanager.notifications.payload

import com.footballmanager.calendar.CurrentMomentHolder
import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.functions.TournamentTodayMatchesFunction
import com.footballmanager.notifications.model.Notification
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class RoundPreviewPayloadGenerator(
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val currentMomentHolder: CurrentMomentHolder,
) {
    fun generate(tournamentId: UUID): Notification {
        val todayMatches = tournamentTodayMatchesFunction.execute(tournamentId)
            ?: throw IllegalStateException("В день матча должен существовать тур")
        val tournament = leagues[tournamentId]!!
        val teamPairs = todayMatches.map { teams[it.homeTeam]!!.name to teams[it.awayTeam]!!.name }
        val maxLen = teamPairs.maxOf { it.first.length }
        val text = teamPairs.joinToString("\n") { (home, away) ->
            "${home.padStart(maxLen)} - $away"
        }
        return Notification(
            title = "Матчи сегодня в ${tournament.name}",
            text = text,
            timestamp = LocalDateTime.now(),
            date = currentMomentHolder.get().toLocalDate(),
        )
    }
}