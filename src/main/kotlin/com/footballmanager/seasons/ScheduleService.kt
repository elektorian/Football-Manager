package com.footballmanager.seasons

import com.footballmanager.entities.Club
import com.footballmanager.entities.match.Match
import com.footballmanager.entities.season.Season
import com.footballmanager.entities.season.schedule.LeagueSchedule
import com.footballmanager.entities.season.schedule.Round
import com.footballmanager.matches.MatchesService
import com.footballmanager.rounds.RoundsService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

@Service
class ScheduleService(
    private val teams: ConcurrentHashMap<UUID, Club>,
    private val matchesService: MatchesService,
    private val roundsService: RoundsService,
) {
    private val schedules = ConcurrentHashMap<UUID, LeagueSchedule>()

    fun getSchedule(id: UUID) = schedules[id]!!

    fun generateLeagueSchedule(season: Season): LeagueSchedule {
        val clubs = season.clubs.shuffled().map { teams[it]!! }
        val n = clubs.size

        val effectiveN = if (n % 2 == 1) n + 1 else n
        val teams: List<Club?> = clubs + List(effectiveN - n) { null }

        val baseDate = Date.from(
            LocalDate.of(season.year, 7, 7)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )
        val springDate = Date.from(
            LocalDate.of(season.year + 1, 3, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        )

        val totalRounds = 2 * (effectiveN - 1)
        val firstHalfCount = (totalRounds * 0.62).roundToInt()
        val secondHalfCount = totalRounds - firstHalfCount

        val dayMs = 24 * 60 * 60 * 1000L
        val firstHalfIntervalMs = 8 * dayMs
        val secondHalfIntervalMs = (7 * dayMs) + (12 * 60 * 60 * 1000L)

        val fixed = teams[0]
        var rest = teams.drop(1)

        fun rotate() {
            rest = listOf(rest.last()) + rest.dropLast(1)
        }

        fun buildRound(arranged: List<Club?>, roundNumber: Int, isLeftHome: Boolean, matchDate: LocalDate): Round {
            val roundId = UUID.randomUUID()

            val matches = buildList {
                for (i in 0 until effectiveN / 2) {
                    val left = arranged[i]
                    val right = arranged[effectiveN - 1 - i]

                    if (left != null && right != null) {
                        val (home, away) = if (isLeftHome) left to right else right to left
                        val match =
                            Match(
                                id = UUID.randomUUID(),
                                date = matchDate,
                                homeTeam = home.id,
                                awayTeam = away.id,
                                round = roundId,
                            )
                        add(match)
                    }
                }
            }
            matches.forEach { matchesService.create(it) }

            val matchesIds = matches.map { it.id }
            season.matches.addAll(matchesIds)
            val round = Round(matchesIds, roundNumber, passed = false, season, roundId)
            roundsService.add(round)
            return round
        }

        val firstHalf = buildList {
            for (roundIndex in 0 until firstHalfCount) {
                val date = Date(baseDate.time + roundIndex * firstHalfIntervalMs).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                add(buildRound(listOf(fixed) + rest, roundIndex + 1, roundIndex % 2 == 0, date))
                rotate()
            }
        }

        val secondHalf = buildList {
            for (roundIndex in 0 until secondHalfCount) {
                val date = Date(springDate.time + roundIndex * secondHalfIntervalMs).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                add(
                    buildRound(
                        listOf(fixed) + rest,
                        firstHalfCount + roundIndex + 1,
                        roundIndex % 2 != 0,
                        date,
                    )
                )
                rotate()
            }
        }

        val schedule = LeagueSchedule(
            id = UUID.randomUUID(),
            rounds = (firstHalf + secondHalf).sortedBy { it.number }.map { it.id },
        )
        schedules[schedule.id] = schedule
        return schedule
    }
}