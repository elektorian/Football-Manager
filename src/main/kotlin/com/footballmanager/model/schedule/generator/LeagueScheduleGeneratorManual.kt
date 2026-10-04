package com.footballmanager.model.schedule.generator

import com.footballmanager.model.match.entity.Match
import com.footballmanager.model.match.repository.MatchRepository
import com.footballmanager.model.schedule.entity.Round
import com.footballmanager.model.schedule.entity.Schedule
import com.footballmanager.model.schedule.repository.RoundRepository
import com.footballmanager.model.schedule.repository.ScheduleRepository
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.LinkedList
import java.util.UUID

@Service
@Primary
class LeagueScheduleGeneratorManual(
    private val scheduleRepository: ScheduleRepository,
    private val roundRepository: RoundRepository,
    private val matchRepository: MatchRepository,
) : LeagueScheduleGenerator {
    override fun generateLeagueSchedule(
        year: Int,
        clubs: List<UUID>
    ): Schedule {
        val roundsTotal = (clubs.size - 1) * 2
        val matchesDates = LinkedList(getMatchesDates(year, roundsTotal).flatMap { d -> (1..clubs.size / 4).map { d } })

        val clubsShuffled = clubs.shuffled()
        val clubsMatches = clubsShuffled.map { LinkedList(clubsShuffled.indices.shuffled()) }

        val rounds = mutableListOf<Round>()
        for (roundNumber in 1..roundsTotal) {
            val homeTeams = if (roundNumber % 2 == 0) {
                clubsShuffled.indices.take(clubsShuffled.size / 2)
            } else {
                clubsShuffled.indices.toList().takeLast(clubsShuffled.size / 2)
            }
            val matchesPerRound = mutableListOf<Match>()
            for (homeTeamIndex in homeTeams) {
                val teamMatches = clubsMatches[homeTeamIndex]
                val homeTeam = clubsShuffled[homeTeamIndex]
                var awayTeamIndex = teamMatches.poll()
                if (awayTeamIndex == homeTeamIndex) awayTeamIndex = teamMatches.poll()
                if (awayTeamIndex == null) continue
                val awayTeam = clubsShuffled[awayTeamIndex]
                val match = Match(
                    homeTeam = homeTeam,
                    awayTeam = awayTeam,
                    id = UUID.randomUUID(),
                    date = matchesDates.poll()
                )
                matchRepository.addMatch(match)
                matchesPerRound.add(match)
            }
            val round = Round(
                id = UUID.randomUUID(),
                stage = roundNumber,
                matches = matchesPerRound.map { it.id },
            )
            roundRepository.add(round)
            rounds.add(round)
        }

        val schedule = Schedule(
            id = UUID.randomUUID(),
            rounds = rounds.map { it.id },
        )
        scheduleRepository.createSchedule(schedule)
        return schedule
    }

    private fun getMatchesDates(
        year: Int,
        roundsTotal: Int,
    ): List<LocalDate> {
        var firstHalfMatchDate =
            LocalDate.of(year, 7, 10)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        var secondHalfMatchDate =
            LocalDate.of(year + 1, 5, 21)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val matchesDatesFirstHalf = LinkedList<LocalDate>()
        val matchesDatesSecondHalf = LinkedList<LocalDate>()
        (roundsTotal / 2 downTo 1).forEach { _ ->
            matchesDatesFirstHalf.addLast(firstHalfMatchDate)
            matchesDatesSecondHalf.addFirst(secondHalfMatchDate)
            firstHalfMatchDate = firstHalfMatchDate.plusDays(7)
            secondHalfMatchDate = secondHalfMatchDate.minusDays(7)
        }

        val secondHalfMatchShiftBorder = LocalDate.of(year + 1, 1, 31)
        val firstHalfMatchShiftBorder = LocalDate.of(year, 12, 31)
        while (true) {
            val nextFreeDateForFirstHalf = matchesDatesFirstHalf.last().plusDays(7)
            if (nextFreeDateForFirstHalf.isAfter(firstHalfMatchShiftBorder)) break
            if (matchesDatesSecondHalf.first().isAfter(secondHalfMatchShiftBorder)) break
            matchesDatesSecondHalf.removeFirst()
            matchesDatesFirstHalf.addLast(nextFreeDateForFirstHalf)
        }

        return shiftDatesInsideWeekend(matchesDatesFirstHalf) + shiftDatesInsideWeekend(matchesDatesSecondHalf)
    }

    private fun shiftDatesInsideWeekend(list: LinkedList<LocalDate>): List<LocalDate> {
        val action = when (list.first().dayOfWeek) {
            DayOfWeek.SUNDAY -> {
                { e: LocalDate -> e.minusDays(1) }
            }
            DayOfWeek.SATURDAY -> {
                { e: LocalDate -> e.plusDays(1) }
            }
            else -> {
                throw UnsupportedOperationException()
            }
        }
        return list.shuffled().flatMap { date -> listOf(date, action(date)) }.sorted()
    }
}