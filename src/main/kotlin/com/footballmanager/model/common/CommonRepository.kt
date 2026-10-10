package com.footballmanager.model.common

import com.footballmanager.model.match.repository.MatchRepository
import com.footballmanager.model.schedule.repository.RoundRepository
import com.footballmanager.model.schedule.repository.ScheduleRepository
import com.footballmanager.model.season.repository.SeasonRepository
import com.footballmanager.model.tournament.entity.Tournament
import com.footballmanager.model.tournament.repository.TournamentRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CommonRepository(
    private val roundRepository: RoundRepository,
    private val matchRepository: MatchRepository,
    private val scheduleRepository: ScheduleRepository,
    private val seasonRepository: SeasonRepository,
    private val tournamentRepository: TournamentRepository,
) {
    fun getTournamentByMatch(match: UUID): Tournament {
        val match = matchRepository.get(match)
        val round = roundRepository.get(match.round)
        val schedule = scheduleRepository.get(round.schedule)
        val season = seasonRepository.get(schedule.season)
        return tournamentRepository.get(season.tournament)
    }
}