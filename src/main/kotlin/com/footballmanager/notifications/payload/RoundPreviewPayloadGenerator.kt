package com.footballmanager.notifications.payload

import com.footballmanager.calendar.CurrentMomentHolder
import com.footballmanager.entities.Club
import com.footballmanager.entities.League
import com.footballmanager.functions.LeagueTableFunction
import com.footballmanager.functions.TournamentCurrentSeasonFunction
import com.footballmanager.functions.TournamentTodayMatchesFunction
import com.footballmanager.notifications.model.Notification
import com.footballmanager.notifications.model.NotificationType
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class RoundPreviewPayloadGenerator(
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
    private val leagueTableFunction: LeagueTableFunction,
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val currentMomentHolder: CurrentMomentHolder,
) {
    fun generate(tournamentId: UUID): Notification {
        val todayMatches = tournamentTodayMatchesFunction.execute(tournamentId)
            ?: throw IllegalStateException("В день матча должен существовать тур")
        val tournament = leagues[tournamentId]!!
        val season = tournamentCurrentSeasonFunction.execute(tournamentId)
        val table = leagueTableFunction.getLeagueTable(tournamentId, season.id)
        val positionByName = table.associate { it.name to it.position }
        return Notification(
            title = "Матчи сегодня в ${tournament.name}",
            type = NotificationType.MATCH_PREVIEW,
            payload = MatchPreviewPayload(
                tournamentName = tournament.name,
                matches = todayMatches.map { match ->
                    val homeName = teams[match.homeTeam]!!.name
                    val awayName = teams[match.awayTeam]!!.name
                    MatchPair(
                        homeTeam = homeName,
                        awayTeam = awayName,
                        homePosition = positionByName[homeName] ?: 0,
                        awayPosition = positionByName[awayName] ?: 0,
                    )
                },
            ),
            timestamp = LocalDateTime.now(),
            date = currentMomentHolder.get().toLocalDate(),
        )
    }
}
