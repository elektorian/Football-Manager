package com.footballmanager.domain.core.notification.payload.roundPreview

import com.footballmanager.domain.core.calendar.CurrentMomentHolder
import com.footballmanager.domain.core.notification.enumeration.NotificationType
import com.footballmanager.domain.core.notification.model.Notification
import com.footballmanager.domain.core.notification.payload.roundPreview.dto.MatchPair
import com.footballmanager.domain.core.notification.payload.roundPreview.dto.RoundPreviewPayload
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.domain.dao.TournamentRepository
import com.footballmanager.domain.functions.LeagueTableFunction
import com.footballmanager.domain.functions.TournamentCurrentSeasonFunction
import com.footballmanager.domain.functions.TournamentTodayMatchesFunction
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.map

@Component
class RoundPreviewPayloadGenerator(
    private val tournamentTodayMatchesFunction: TournamentTodayMatchesFunction,
    private val tournamentCurrentSeasonFunction: TournamentCurrentSeasonFunction,
    private val leagueTableFunction: LeagueTableFunction,
    private val tournamentRepository: TournamentRepository,
    private val teamRepository: TeamRepository,
    private val currentMomentHolder: CurrentMomentHolder,
) {
    fun generate(tournamentId: UUID): Notification {
        val todayMatches = tournamentTodayMatchesFunction.execute(tournamentId)
            ?: throw IllegalStateException("В день матча должен существовать тур")
        val tournament = tournamentRepository.get(tournamentId)
        val season = tournamentCurrentSeasonFunction.execute(tournamentId)
        val table = leagueTableFunction.getLeagueTable(tournamentId, season.id)
        val positionByName = table.associate { it.name to it.position }
        return Notification(
            title = "Матчи сегодня в ${tournament.name}",
            type = NotificationType.MATCH_PREVIEW,
            payload = RoundPreviewPayload(
                tournamentName = tournament.name,
                matches = todayMatches.map { match ->
                    val homeName = teamRepository.get(match.homeTeam).name
                    val awayName = teamRepository.get(match.awayTeam).name
                    MatchPair(
                        homeTeamId = match.homeTeam,
                        homeTeam = homeName,
                        awayTeamId = match.awayTeam,
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
