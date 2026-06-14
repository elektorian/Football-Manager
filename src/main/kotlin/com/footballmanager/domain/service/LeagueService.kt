package com.footballmanager.domain.service

import com.footballmanager.domain.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.atomic.AtomicInteger

class LeagueService {
    private val matchIdGen = AtomicInteger(1)

    companion object {
        val DEFAULT_TEAM_NAMES = listOf(
            "Zenit", "Spartak", "CSKA", "Lokomotiv", "Krasnodar", 
            "Dynamo", "Rostov", "Krylya Sovetov", "Akhmat", "Ural", 
            "Fakel", "Pari NB", "Torpedo", "Rubin", "Khimki", "FC Rostov 2"
        )
    }

    fun generateRPL(): Pair<League, List<Match>> {
        val teamNames = DEFAULT_TEAM_NAMES
        val teams = teamNames.mapIndexed { index, name -> Team(index.toString(), name) }
        val league = League("rpl", "Russian Premier League", teams.map { it.id })
        
        val matches = generateDoubleRoundRobin(league)
        return Pair(league, matches)
    }

    private fun generateDoubleRoundRobin(league: League): List<Match> {
        val teamIds = league.teamIds
        val n = teamIds.size // 16
        val participants = teamIds.toMutableList()
        val singleRoundPairs = mutableListOf<List<String>>()

        // Circle Method for Single Round Robin (n-1 rounds)
        for (r in 0 until n - 1) {
            val roundMatches = mutableListOf<List<String>>()
            for (i in 0 until n / 2) {
                roundMatches.add(listOf(participants[i], participants[n - 1 - i]))
            }
            singleRoundPairs.add(roundMatches.flatten()) // This is wrong, should store as pairs
            
            // Rotation: Keep first element fixed, rotate others
            val last = participants.removeAt(n - 1)
            participants.add(1, last)
        }

        // Actually let's produce a list of Pairs for each round
        val roundsPairs = mutableListOf<List<Pair<String, String>>>()
        val p = teamIds.toMutableList()
        for (r in 0 until n - 1) {
            val currentRound = mutableListOf<Pair<String, String>>()
            for (i in 0 until n / 2) {
                currentRound.add(p[i] to p[n - 1 - i])
            }
            roundsPairs.add(currentRound)
            val last = p.removeAt(n - 1)
            p.add(1, last)
        }

        val allMatches = mutableListOf<Match>()
        val startDate = LocalDate.of(2020, 7, 1) // Base date for start of season

        // Generate 30 rounds: first 15 then reverse roles for next 15
        for (r in 0 until 30) {
            val roundNum = r + 1
            val isSecondHalf = r >= n - 1
            val pairIndex = if (isSecondHalf) r - (n - 1) else r
            val pairs = roundsPairs[pairIndex]
            
            // Date: every Sunday starting from the first available Sunday after startDate
            val matchDate = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .plusWeeks(r.toLong())

            for (pair in pairs) {
                val home = if (isSecondHalf) pair.second else pair.first
                val away = if (isSecondHalf) pair.first else pair.second
                allMatches.add(Match(
                    id = (matchIdGen.getAndIncrement()).toString(),
                    leagueId = league.id,
                    round = roundNum,
                    teamAId = home,
                    teamBId = away,
                    scheduledDate = matchDate
                ))
            }
        }

        return allMatches
    }

    fun calculateStandings(leagueId: String, matches: List<Match>, teams: List<Team>): List<StandingsEntry> {
        val scores = mutableMapOf<String, TeamStats>()
        teams.forEach { scores[it.id] = TeamStats(it.id) }

        matches.filter { it.leagueId == leagueId && it.result != null }.forEach { match ->
            val res = match.result!!
            val statsA = scores[match.teamAId]!!
            val statsB = scores[match.teamBId]!!

            statsA.played++
            statsB.played++
            statsA.gf += res.scoreA
            statsA.ga += res.scoreB
            statsB.gf += res.scoreB
            statsB.ga += res.scoreA

            if (res.scoreA > res.scoreB) {
                statsA.won++
                statsA.points += 3
                statsB.lost++
            } else if (res.scoreA < res.scoreB) {
                statsB.won++
                statsB.points += 3
                statsA.lost++
            } else {
                statsA.drawn++
                statsB.drawn++
                statsA.points += 1
                statsB.points += 1
            }
        }

        return scores.values.map { it.toEntry() }
            .sortedWith(compareByDescending<StandingsEntry> { it.points }
                .thenByDescending { it.gf - it.ga }
                .thenByDescending { it.gf })
    }

    private class TeamStats(val id: String) {
        var played = 0; var won = 0; var drawn = 0; var lost = 0; var gf = 0; var ga = 0; var points = 0
        fun toEntry() = StandingsEntry(id, played, won, drawn, lost, gf, ga, points)
    }
}

data class StandingsEntry(
    val teamId: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val gf: Int,
    val ga: Int,
    val points: Int
)