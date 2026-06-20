package com.footballmanager.domain.service

import com.footballmanager.domain.model.LeagueSchedule
import com.footballmanager.domain.model.Match
import com.footballmanager.domain.model.Round
import com.footballmanager.domain.model.Season
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import kotlin.math.roundToInt

data class GeneratedSchedule(
    val schedule: LeagueSchedule,
    val rounds: List<Round>,
    val matches: List<Match>,
)

class ScheduleGenerator {
    fun generateLeagueSchedule(season: Season, teams: List<UUID>): GeneratedSchedule {
        val shuffledTeams = teams.shuffled()
        val n = shuffledTeams.size
        val effectiveN = if (n % 2 == 1) n + 1 else n
        val allTeams: List<UUID?> = shuffledTeams + List(effectiveN - n) { null }

        val baseDate = LocalDate.of(season.year, 7, 7)
        val springDate = LocalDate.of(season.year + 1, 3, 1)

        val totalRounds = 2 * (effectiveN - 1)
        val firstHalfCount = (totalRounds * 0.62).roundToInt()
        val secondHalfCount = totalRounds - firstHalfCount

        val dayMs = 24 * 60 * 60 * 1000L
        val firstHalfIntervalMs = 8 * dayMs
        val secondHalfIntervalMs = (7 * dayMs) + (12 * 60 * 60 * 1000L)

        val baseDateMs = Date.from(baseDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).time
        val springDateMs = Date.from(springDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).time

        val fixed = allTeams[0]
        var rest = allTeams.drop(1)

        fun rotate() {
            rest = listOf(rest.last()) + rest.dropLast(1)
        }

        val allMatches = mutableListOf<Match>()
        val allRounds = mutableListOf<Round>()

        fun buildRound(arranged: List<UUID?>, roundNumber: Int, isLeftHome: Boolean, matchDate: LocalDate): Round {
            val roundId = UUID.randomUUID()
            val roundMatches = buildList {
                for (i in 0 until effectiveN / 2) {
                    val left = arranged[i]
                    val right = arranged[effectiveN - 1 - i]
                    if (left != null && right != null) {
                        val (home, away) = if (isLeftHome) left to right else right to left
                        add(
                            Match(
                                id = UUID.randomUUID(),
                                date = matchDate,
                                homeTeam = home,
                                awayTeam = away,
                                round = roundId,
                            )
                        )
                    }
                }
            }
            allMatches.addAll(roundMatches)
            val round = Round(
                matches = roundMatches.map { it.id },
                number = roundNumber,
                passed = false,
                seasonId = season.id,
                id = roundId,
            )
            allRounds.add(round)
            return round
        }

        val fixedNonNull = fixed ?: UUID.randomUUID()
        val restNonNull = rest.filterNotNull()

        val firstHalf = buildList {
            for (roundIndex in 0 until firstHalfCount) {
                val date = Date(baseDateMs + roundIndex * firstHalfIntervalMs)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                add(buildRound(listOfNotNull(fixed) + restNonNull, roundIndex + 1, roundIndex % 2 == 0, date))
                rotate()
            }
        }

        val secondHalf = buildList {
            for (roundIndex in 0 until secondHalfCount) {
                val date = Date(springDateMs + roundIndex * secondHalfIntervalMs)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                add(
                    buildRound(
                        listOfNotNull(fixed) + restNonNull,
                        firstHalfCount + roundIndex + 1,
                        roundIndex % 2 != 0,
                        date,
                    )
                )
                rotate()
            }
        }

        return GeneratedSchedule(
            schedule = LeagueSchedule(
                id = UUID.randomUUID(),
                rounds = (firstHalf + secondHalf).sortedBy { it.number }.map { it.id },
            ),
            rounds = allRounds,
            matches = allMatches,
        )
    }
}
