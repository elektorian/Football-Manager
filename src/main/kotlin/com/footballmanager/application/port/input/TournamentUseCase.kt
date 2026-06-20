package com.footballmanager.application.port.input

import com.footballmanager.application.dto.LeagueTeamInfoDto
import com.footballmanager.application.dto.TournamentDto
import java.util.UUID

interface TournamentUseCase {
    fun getLeagueTable(leagueId: UUID, seasonId: UUID?): Collection<LeagueTeamInfoDto>
    fun getTournament(id: UUID): TournamentDto
}
