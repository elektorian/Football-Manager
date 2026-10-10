package com.footballmanager.messages

import com.footballmanager.messages.entity.matchOverview.MatchOverview
import com.footballmanager.messages.entity.matchOverview.MatchOverviewMessage
import com.footballmanager.model.club.repository.ClubRepository
import com.footballmanager.model.match.entity.Match
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MessageProducer(
    private val messageService: MessageService,
    private val clubRepository: ClubRepository,
) {
    fun registerMessageOverviewMessage(matches: List<Match>) {
        val matchOverviews = matches.map {
            val homeClub = clubRepository.getClub(it.homeTeam)
            val awayClub = clubRepository.getClub(it.awayTeam)
            MatchOverview(
                matchId = it.id,
                homeTeamName = homeClub.name,
                awayTeamName = awayClub.name,
                date = it.date,
                homeTeamScore = it.result!!.homeTeamResult.scored,
                awayTeamScore = it.result!!.awayTeamResult.scored,
            )
        }
        val matchOverview = MatchOverviewMessage(
            id = UUID.randomUUID(),
            type = MessageType.MATCHES_OVERVIEW,
            matches = matchOverviews,
        )
        messageService.register(matchOverview)
    }
}