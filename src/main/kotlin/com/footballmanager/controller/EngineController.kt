package com.footballmanager.controller

import com.footballmanager.api.ClubInfo
import com.footballmanager.api.EngineStateResponse
import com.footballmanager.api.MatchInfo
import com.footballmanager.api.StandingRow
import com.footballmanager.domain.League
import com.footballmanager.engine.GameEngine
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/engine")
class EngineController(
    private val engine: GameEngine
) {

    @GetMapping("/state")
    fun getState(): EngineStateResponse {
        return buildState(events = null)
    }

    @PostMapping("/advance")
    fun advance(@RequestParam(defaultValue = "1") days: Int): EngineStateResponse {
        val events = engine.advance(days)
        return buildState(
            events = events.map { it::class.simpleName ?: "Unknown" }
        )
    }

    private fun buildState(events: List<String>?): EngineStateResponse {
        val world = engine.world
        val session = world.session
        val club = session?.let { world.clubs[it.clubId] }

        val clubInfo = club?.let { ClubInfo(it.id, it.name, it.shortName) }

        val clubId = club?.id

        val league = world.competitions.values
            .filterIsInstance<League>()
            .firstOrNull()

        val nextMatch = league?.let { l ->
            l.fixtures
                .filter { !it.played && (clubId == null || it.homeClubId == clubId || it.awayClubId == clubId) }
                .minByOrNull { it.date }
        }

        val nextMatchInfo = nextMatch?.let { f ->
            val opponent = world.clubs[if (f.homeClubId == clubId) f.awayClubId else f.homeClubId]
            val venue = if (f.homeClubId == clubId) "H" else "A"
            MatchInfo(
                fixtureId = f.id,
                opponent = opponent?.name ?: "Unknown",
                venue = venue,
                date = f.date.toString(),
                competition = league?.name ?: ""
            )
        }

        val table = league?.standings?.mapNotNull { s ->
            val c = world.clubs[s.clubId] ?: return@mapNotNull null
            StandingRow(
                position = 0,
                clubId = s.clubId,
                clubName = c.name,
                played = s.played,
                wins = s.wins,
                draws = s.draws,
                losses = s.losses,
                goalsFor = s.goalsFor,
                goalsAgainst = s.goalsAgainst,
                points = s.points,
                gd = s.goalDifference,
                form = s.form
            )
        }?.sortedByDescending { it.points }?.mapIndexed { i, row -> row.copy(position = i + 1) }
            ?: emptyList()

        val recentForm = clubId?.let { id ->
            league?.standings?.firstOrNull { it.clubId == id }?.form
        } ?: emptyList()

        val upcoming = league?.fixtures
            ?.filter { !it.played && (clubId == null || it.homeClubId == clubId || it.awayClubId == clubId) }
            ?.sortedBy { it.date }
            ?.take(5)
            ?.map { f ->
                val opponent = world.clubs[if (f.homeClubId == clubId) f.awayClubId else f.homeClubId]
                val venue = if (f.homeClubId == clubId) "H" else "A"
                MatchInfo(
                    fixtureId = f.id,
                    opponent = opponent?.name ?: "Unknown",
                    venue = venue,
                    date = f.date.toString(),
                    competition = league.name
                )
            } ?: emptyList()

        return EngineStateResponse(
            date = world.clock.currentDate.toString(),
            club = clubInfo,
            nextMatch = nextMatchInfo,
            leagueTable = table,
            recentForm = recentForm,
            upcomingFixtures = upcoming,
            events = events
        )
    }
}
