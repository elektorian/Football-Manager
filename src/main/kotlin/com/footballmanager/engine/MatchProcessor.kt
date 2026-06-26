package com.footballmanager.engine

import com.footballmanager.domain.League
import com.footballmanager.domain.PersonRole
import com.footballmanager.domain.Standing
import org.springframework.stereotype.Component
import java.time.LocalDate
import kotlin.random.Random

@Component
class MatchProcessor : DayProcessor {

    override val priority: Int = 100

    private val rng = Random

    override fun process(
        date: LocalDate,
        scheduled: List<ScheduledEvent>,
        world: GameWorld
    ): List<GameEvent> {
        val events = mutableListOf<GameEvent>()

        for (comp in world.competitions.values) {
            if (comp !is League) continue
            val standings = comp.standings
            for (fixture in comp.fixtures) {
                if (fixture.played || fixture.date != date) continue

                val (homeGoals, awayGoals) = simulateMatch(fixture, world)
                fixture.homeGoals = homeGoals
                fixture.awayGoals = awayGoals
                fixture.played = true

                updateStandings(standings, fixture.homeClubId, fixture.awayClubId, homeGoals, awayGoals)

                events.add(
                    MatchPlayedEvent(
                        date = date,
                        fixtureId = fixture.id,
                        homeClubId = fixture.homeClubId,
                        awayClubId = fixture.awayClubId,
                        homeGoals = homeGoals,
                        awayGoals = awayGoals
                    )
                )
            }
        }

        return events
    }

    private fun simulateMatch(
        fixture: com.footballmanager.domain.Fixture,
        world: GameWorld
    ): Pair<Int, Int> {
        val homeStrength = clubStrength(fixture.homeClubId, world)
        val awayStrength = clubStrength(fixture.awayClubId, world)
        val homeAdvantage = 5

        val homeExpected = (homeStrength + homeAdvantage) / 100.0 * 1.5
        val awayExpected = awayStrength / 100.0 * 1.3

        val homeGoals = poissonGoals(homeExpected, rng)
        val awayGoals = poissonGoals(awayExpected, rng)

        return Pair(homeGoals, awayGoals)
    }

    private fun clubStrength(clubId: Long, world: GameWorld): Int {
        val club = world.clubs[clubId] ?: return 100
        val players = club.players.filter { it.role == PersonRole.Player }
        if (players.isEmpty()) return 100
        return players.sumOf { it.ability } / players.size
    }

    private fun poissonGoals(lambda: Double, rng: Random): Int {
        var goals = 0
        var prob = kotlin.math.exp(-lambda)
        var cumulative = prob
        val sample = rng.nextDouble()
        while (cumulative < sample) {
            goals++
            prob *= lambda / goals
            cumulative += prob
        }
        return goals
    }

    private fun updateStandings(
        standings: MutableList<Standing>,
        homeId: Long,
        awayId: Long,
        homeGoals: Int,
        awayGoals: Int
    ) {
        updateStanding(standings, homeId, homeGoals, awayGoals)
        updateStanding(standings, awayId, awayGoals, homeGoals)
    }

    private fun updateStanding(
        standings: MutableList<Standing>,
        clubId: Long,
        goalsFor: Int,
        goalsAgainst: Int
    ) {
        val idx = standings.indexOfFirst { it.clubId == clubId }
        if (idx == -1) return

        val old = standings[idx]
        val played = old.played + 1
        val wins = old.wins + if (goalsFor > goalsAgainst) 1 else 0
        val draws = old.draws + if (goalsFor == goalsAgainst) 1 else 0
        val losses = old.losses + if (goalsFor < goalsAgainst) 1 else 0
        val gf = old.goalsFor + goalsFor
        val ga = old.goalsAgainst + goalsAgainst
        val result = when {
            goalsFor > goalsAgainst -> "W"
            goalsFor == goalsAgainst -> "D"
            else -> "L"
        }
        val form = (old.form + result).takeLast(5)

        standings[idx] = old.copy(
            played = played,
            wins = wins,
            draws = draws,
            losses = losses,
            goalsFor = gf,
            goalsAgainst = ga,
            form = form
        )
    }
}
