package com.footballmanager.messages

import com.footballmanager.messages.entity.matchOverview.MatchOverview
import com.footballmanager.messages.entity.matchOverview.MatchOverviewMessage
import com.footballmanager.model.club.repository.ClubRepository
import com.footballmanager.model.common.CommonRepository
import com.footballmanager.model.match.entity.Match
import com.footballmanager.time.TimeContext
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MessageProducer(
    private val messageService: MessageService,
    private val clubRepository: ClubRepository,
    private val timeContext: TimeContext,
    private val commonRepository: CommonRepository,
) {
    fun registerMessageOverviewMessage(matches: List<Match>) {
        if (matches.isEmpty()) return
        val tournaments = matches.map { commonRepository.getTournamentByMatch(it.id) }
        if (tournaments.distinctBy { it.id }.size != 1) {
            throw IllegalStateException("Попытка сформировать сообщение о матчах для нескольких лиг сразу")
        }
        val tournament = tournaments.first()
        val matchOverviews = matches.map {
            val homeClub = clubRepository.getClub(it.homeTeam)
            val awayClub = clubRepository.getClub(it.awayTeam)
            MatchOverview(
                matchId = it.id,
                homeTeamName = homeClub.name,
                awayTeamName = awayClub.name,
                date = it.date,
                homeTeamScore = it.result?.homeTeamResult?.scored,
                awayTeamScore = it.result?.awayTeamResult?.scored,
            )
        }
        val matchOverview = MatchOverviewMessage(
            id = UUID.randomUUID(),
            type = MessageType.MATCHES_OVERVIEW,
            timestamp = timeContext.getTimestamp(),
            matches = matchOverviews,
            title = tournament.name,
        )
        messageService.register(matchOverview)
    }
}