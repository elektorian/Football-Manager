package com.footballmanager.model.schedule.generator

import com.footballmanager.model.match.MatchService
import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.schedule.RoundService
import com.footballmanager.model.schedule.ScheduleService
import com.footballmanager.model.schedule.entity.Round
import com.footballmanager.model.schedule.entity.Schedule
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

@Component
@Primary
class LeagueScheduleGeneratorAI(
    private val scheduleService: ScheduleService,
    private val roundService: RoundService,
    private val matchService: MatchService,
) : LeagueScheduleGenerator {

    override fun generateLeagueSchedule(
        clubs: List<UUID>,
        seasonId: UUID,
        year: Int,
    ): Schedule {
        require(clubs.size >= 4 && clubs.size % 2 == 0) {
            "Number of clubs must be even and at least 4, got: ${clubs.size}"
        }

        val n = clubs.size
        val totalRounds = 2 * (n - 1)

        val matchDates = generateMatchDates(year, totalRounds)
        val firstHalfRoundCount = matchDates.firstHalf.size
        val secondHalfRoundCount = totalRounds - firstHalfRoundCount

        val firstHalfPairings = generateRoundRobinFirstHalf(clubs, firstHalfRoundCount)
        val secondHalfPairings = generateRoundRobinSecondHalf(
            clubs = clubs,
            rounds = secondHalfRoundCount,
            firstHalfRounds = firstHalfRoundCount,
        )

        val allDates = matchDates.firstHalf + matchDates.secondHalf
        val allPairings = firstHalfPairings + secondHalfPairings

        require(allDates.size >= allPairings.size) {
            "Not enough match dates for ${allPairings.size} rounds, only ${allDates.size} available"
        }

        val scheduleId = UUID.randomUUID()
        val rounds = mutableListOf<Round>()

        for (i in allPairings.indices) {
            val pairing = allPairings[i]
            val weekend = allDates[i]
            val roundId = UUID.randomUUID()
            val saturdayMatches = pairing.take(n / 4).shuffled(ThreadLocalRandom.current())
            val sundayMatches = pairing.drop(n / 4).shuffled(ThreadLocalRandom.current())

            val dayMatches = saturdayMatches.map { (home, away) ->
                matchService.create(
                    id = UUID.randomUUID(),
                    date = weekend.saturday,
                    homeTeam = home,
                    awayTeam = away,
                    round = roundId,
                )
            } + sundayMatches.map { (home, away) ->
                matchService.create(
                    id = UUID.randomUUID(),
                    date = weekend.sunday,
                    homeTeam = home,
                    awayTeam = away,
                    round = roundId,
                )
            }

            val round = roundService.createRound(
                id = roundId,
                stage = i + 1,
                matches = dayMatches.map { it.id },
                schedule = scheduleId,
            )
            rounds.add(round)
        }

        return scheduleService.createSchedule(
            id = scheduleId,
            rounds = rounds.map { it.id },
            season = seasonId,
        )
    }

    private fun generateMatchDates(year: Int, roundsNeeded: Int): MatchDates {
        val firstHalfRounds = roundsNeeded / 2
        val secondHalfRounds = roundsNeeded - firstHalfRounds

        val firstHalf = generateHalfDates(
            startDate = LocalDate.of(year, Month.JULY, 10),
            endDate = LocalDate.of(year, Month.DECEMBER, 31),
            rounds = firstHalfRounds,
        )

        val secondHalf = generateHalfDates(
            startDate = LocalDate.of(year + 1, Month.FEBRUARY, 1),
            endDate = LocalDate.of(year + 1, Month.MAY, 21),
            rounds = secondHalfRounds,
        )

        return MatchDates(firstHalf, secondHalf)
    }

    private fun generateHalfDates(
        startDate: LocalDate,
        endDate: LocalDate,
        rounds: Int,
    ): List<WeekendDates> {
        val allWeekends = mutableListOf<LocalDate>()
        var current = startDate
        while (current <= endDate) {
            if (current.dayOfWeek == DayOfWeek.SATURDAY || current.dayOfWeek == DayOfWeek.SUNDAY) {
                allWeekends.add(current)
            }
            current = current.plusDays(1)
        }

        require(allWeekends.size * 2 >= rounds) {
            "Not enough weekends between $startDate and $endDate for $rounds rounds (max ${allWeekends.size * 2}, found ${allWeekends.size})"
        }

        val shuffled = allWeekends.shuffled(ThreadLocalRandom.current())

        return (0 until rounds).map { i ->
            val reference = shuffled[i % shuffled.size]
            val saturday = if (reference.dayOfWeek == DayOfWeek.SATURDAY) reference else reference.plusDays(1)
            WeekendDates(saturday = saturday, sunday = saturday.plusDays(1))
        }
    }

    private fun generateRoundRobinFirstHalf(
        clubs: List<UUID>,
        rounds: Int,
    ): List<List<Pair<UUID, UUID>>> {
        val n = clubs.size
        val fixedTeam = clubs[0]
        val movable = clubs.subList(1, n)
        val indices = movable.indices.toMutableList()

        val pairings = mutableListOf<List<Pair<UUID, UUID>>>()

        for (round in 0 until rounds) {
            val pairing = mutableListOf<Pair<UUID, UUID>>()
            pairing.add(fixedTeam to movable[indices[0]])

            for (i in 1 until n / 2) {
                val home = movable[indices[i]]
                val away = movable[indices[n - 1 - i]]
                pairing.add(home to away)
            }

            pairings.add(pairing.toList())

            if (round < rounds - 1) {
                mirrorIndices(indices)
            }
        }

        return pairings
    }

    private fun generateRoundRobinSecondHalf(
        clubs: List<UUID>,
        rounds: Int,
        firstHalfRounds: Int,
    ): List<List<Pair<UUID, UUID>>> {
        val n = clubs.size
        val fixedTeam = clubs[0]
        val movable = clubs.subList(1, n)
        var indices = movable.indices.toMutableList()

        for (round in 0 until firstHalfRounds) {
            mirrorIndices(indices)
        }

        val pairings = mutableListOf<List<Pair<UUID, UUID>>>()

        for (round in 0 until rounds) {
            val pairing = mutableListOf<Pair<UUID, UUID>>()
            pairing.add(fixedTeam to movable[indices[0]])

            for (i in 1 until n / 2) {
                val home = movable[indices[n - 1 - i]]
                val away = movable[indices[i]]
                pairing.add(home to away)
            }

            pairings.add(pairing.toList())

            if (round < rounds - 1) {
                mirrorIndices(indices)
            }
        }

        return pairings
    }

    private fun mirrorIndices(indices: MutableList<Int>) {
        val n = indices.size
        val mirrored = indices.toMutableList()
        for (i in 1 until n / 2) {
            val temp = mirrored[i]
            mirrored[i] = mirrored[n - 1 - i]
            mirrored[n - 1 - i] = temp
        }
        indices.clear()
        indices.addAll(mirrored)
    }

    private data class MatchDates(
        val firstHalf: List<WeekendDates>,
        val secondHalf: List<WeekendDates>,
    )

    private data class WeekendDates(
        val saturday: LocalDate,
        val sunday: LocalDate,
    )
}
