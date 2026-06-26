package com.footballmanager.data

import com.footballmanager.domain.Club
import com.footballmanager.domain.Competition
import com.footballmanager.domain.Contract
import com.footballmanager.domain.Fixture
import com.footballmanager.domain.GameSession
import com.footballmanager.domain.League
import com.footballmanager.domain.Person
import com.footballmanager.domain.PersonRole
import com.footballmanager.domain.Position
import com.footballmanager.domain.Standing
import com.footballmanager.engine.GameWorld
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SeedDataGenerator {

    fun populate(world: GameWorld) {
        val date = world.clock.currentDate
        val clubs = buildClubs(date)
        clubs.forEach { world.clubs[it.id] = it }

        val league = buildLeague(clubs, date)
        world.competitions[league.id] = league

        world.session = GameSession(
            manager = createManager(date),
            clubId = 1L
        )
    }

    private fun buildClubs(date: LocalDate): List<Club> = CLUBS.map { (id, name, short, players) ->
        Club(
            id = id,
            name = name,
            shortName = short,
            balance = 50_000_000 + (id * 20_000_000),
            wageBudget = 2_000_000 + (id * 500_000),
            players = players.map { p -> buildPlayer(p, id, date) }.toMutableList(),
            competitionIds = mutableListOf(1L)
        )
    }

    private fun buildPlayer(p: PlayerSeed, clubId: Long, date: LocalDate): Person = Person(
        id = p.id,
        name = p.name,
        age = p.age,
        nationality = "England",
        role = PersonRole.Player,
        positions = p.positions,
        ability = p.ability,
        potential = p.potential,
        contract = Contract(
            clubId = clubId,
            wageWeekly = p.ability * 80,
            signedDate = date.minusMonths(6),
            expiryDate = date.plusYears(3 + (p.id % 3).toLong())
        )
    )

    private fun buildLeague(clubs: List<Club>, date: LocalDate): League {
        val clubIds = clubs.map { it.id }
        val standings = clubIds.map { cid -> Standing(clubId = cid) }.toMutableList()
        val fixtures = generateFixtures(clubIds, date).toMutableList()

        return League(
            id = 1L,
            name = "Premier League",
            season = 2024,
            clubIds = clubIds,
            fixtures = fixtures,
            standings = standings
        )
    }

    private fun generateFixtures(clubIds: List<Long>, seasonStart: LocalDate): List<Fixture> {
        val n = clubIds.size
        val fixtures = mutableListOf<Fixture>()
        val fixed = clubIds[0]
        val rotating = clubIds.drop(1).toMutableList()
        var fixtureId = 1L

        // 10 rounds: 5 home + 5 away
        repeat(2) { half ->
            repeat(n - 1) { round ->
                val pairs = listOf(
                    fixed to rotating[0],
                    rotating[1] to rotating[2],
                    rotating[3] to rotating[4]
                )
                for ((home, away) in pairs) {
                    val (h, a) = if (half == 0) home to away else away to home
                    val matchDate = seasonStart.plusDays((round + half * (n - 1)).toLong() * 7)
                    fixtures.add(
                        Fixture(
                            id = fixtureId++,
                            competitionId = 1L,
                            date = matchDate,
                            homeClubId = h,
                            awayClubId = a
                        )
                    )
                }
                rotating.add(rotating.removeAt(0))
            }
        }
        return fixtures
    }

    private fun createManager(date: LocalDate): Person = Person(
        id = 0L,
        name = "Alex Ferguson",
        age = 55,
        nationality = "Scotland",
        role = PersonRole.Manager,
        positions = listOf(Position.CM),
        ability = 180,
        potential = 190,
        contract = Contract(
            clubId = 1L,
            wageWeekly = 200_000,
            signedDate = date.minusMonths(12),
            expiryDate = date.plusYears(5)
        )
    )

    private data class PlayerSeed(
        val id: Long,
        val name: String,
        val age: Int,
        val positions: List<Position>,
        val ability: Int,
        val potential: Int
    )

    companion object {
        private val CLUBS = listOf(
            ClubSeed(1L, "Manchester United", "MUN", listOf(
                PlayerSeed(101, "David De Gea", 33, listOf(Position.GK), 155, 165),
                PlayerSeed(102, "Andre Onana", 28, listOf(Position.GK), 150, 160),
                PlayerSeed(103, "Lisandro Martinez", 26, listOf(Position.CB), 152, 165),
                PlayerSeed(104, "Raphael Varane", 31, listOf(Position.CB), 158, 175),
                PlayerSeed(105, "Harry Maguire", 31, listOf(Position.CB), 145, 160),
                PlayerSeed(106, "Victor Lindelof", 29, listOf(Position.CB), 140, 152),
                PlayerSeed(107, "Luke Shaw", 28, listOf(Position.LB), 148, 160),
                PlayerSeed(108, "Diogo Dalot", 25, listOf(Position.RB, Position.LB), 144, 158),
                PlayerSeed(109, "Aaron Wan-Bissaka", 26, listOf(Position.RB), 140, 152),
                PlayerSeed(110, "Casemiro", 32, listOf(Position.DM, Position.CM), 160, 175),
                PlayerSeed(111, "Bruno Fernandes", 29, listOf(Position.AM, Position.CM), 165, 170),
                PlayerSeed(112, "Mason Mount", 25, listOf(Position.CM, Position.AM), 148, 165),
                PlayerSeed(113, "Kobbie Mainoo", 19, listOf(Position.CM, Position.DM), 140, 180),
                PlayerSeed(114, "Christian Eriksen", 32, listOf(Position.CM, Position.AM), 150, 165),
                PlayerSeed(115, "Marcus Rashford", 26, listOf(Position.LW, Position.ST), 155, 170),
                PlayerSeed(116, "Alejandro Garnacho", 20, listOf(Position.LW, Position.RW), 142, 175),
                PlayerSeed(117, "Antony", 24, listOf(Position.RW, Position.LW), 140, 158),
                PlayerSeed(118, "Rasmus Hojlund", 21, listOf(Position.ST), 145, 172),
                PlayerSeed(119, "Jadon Sancho", 24, listOf(Position.LW, Position.RW), 145, 165),
                PlayerSeed(120, "Scott McTominay", 27, listOf(Position.CM, Position.DM), 143, 152),
            )),
            ClubSeed(2L, "Arsenal", "ARS", listOf(
                PlayerSeed(201, "Aaron Ramsdale", 26, listOf(Position.GK), 148, 160),
                PlayerSeed(202, "David Raya", 28, listOf(Position.GK), 150, 158),
                PlayerSeed(203, "William Saliba", 23, listOf(Position.CB), 155, 178),
                PlayerSeed(204, "Gabriel Magalhaes", 26, listOf(Position.CB), 150, 162),
                PlayerSeed(205, "Ben White", 26, listOf(Position.CB, Position.RB), 148, 158),
                PlayerSeed(206, "Jakub Kiwior", 24, listOf(Position.CB), 135, 155),
                PlayerSeed(207, "Oleksandr Zinchenko", 27, listOf(Position.LB, Position.CM), 147, 158),
                PlayerSeed(208, "Takehiro Tomiyasu", 25, listOf(Position.RB, Position.LB), 142, 152),
                PlayerSeed(209, "Jurrien Timber", 23, listOf(Position.RB, Position.CB), 145, 168),
                PlayerSeed(210, "Declan Rice", 25, listOf(Position.DM, Position.CM), 162, 172),
                PlayerSeed(211, "Martin Odegaard", 25, listOf(Position.AM, Position.CM), 160, 170),
                PlayerSeed(212, "Thomas Partey", 31, listOf(Position.DM, Position.CM), 152, 165),
                PlayerSeed(213, "Emile Smith Rowe", 23, listOf(Position.AM, Position.CM), 140, 162),
                PlayerSeed(214, "Bukayo Saka", 22, listOf(Position.RW, Position.LW), 163, 180),
                PlayerSeed(215, "Gabriel Martinelli", 23, listOf(Position.LW, Position.RW), 155, 172),
                PlayerSeed(216, "Leandro Trossard", 29, listOf(Position.LW, Position.AM), 148, 155),
                PlayerSeed(217, "Gabriel Jesus", 27, listOf(Position.ST, Position.RW), 158, 168),
                PlayerSeed(218, "Eddie Nketiah", 25, listOf(Position.ST), 140, 152),
                PlayerSeed(219, "Kai Havertz", 25, listOf(Position.AM, Position.ST), 150, 165),
                PlayerSeed(220, "Reiss Nelson", 24, listOf(Position.RW, Position.LW), 135, 148),
            )),
            ClubSeed(3L, "Chelsea", "CHE", listOf(
                PlayerSeed(301, "Robert Sanchez", 26, listOf(Position.GK), 140, 152),
                PlayerSeed(302, "Djordje Petrovic", 24, listOf(Position.GK), 135, 155),
                PlayerSeed(303, "Thiago Silva", 39, listOf(Position.CB), 155, 175),
                PlayerSeed(304, "Levi Colwill", 21, listOf(Position.CB), 140, 172),
                PlayerSeed(305, "Axel Disasi", 26, listOf(Position.CB), 140, 152),
                PlayerSeed(306, "Benoit Badiashile", 23, listOf(Position.CB), 142, 162),
                PlayerSeed(307, "Ben Chilwell", 27, listOf(Position.LB, Position.LW), 148, 162),
                PlayerSeed(308, "Reece James", 24, listOf(Position.RB, Position.CB), 152, 170),
                PlayerSeed(309, "Malo Gusto", 21, listOf(Position.RB), 138, 165),
                PlayerSeed(310, "Moises Caicedo", 22, listOf(Position.DM, Position.CM), 155, 175),
                PlayerSeed(311, "Enzo Fernandez", 23, listOf(Position.CM, Position.AM), 158, 172),
                PlayerSeed(312, "Conor Gallagher", 24, listOf(Position.CM), 145, 155),
                PlayerSeed(313, "Romeo Lavia", 20, listOf(Position.DM, Position.CM), 135, 168),
                PlayerSeed(314, "Cole Palmer", 22, listOf(Position.RW, Position.AM), 155, 175),
                PlayerSeed(315, "Raheem Sterling", 29, listOf(Position.LW, Position.RW), 155, 168),
                PlayerSeed(316, "Mykhailo Mudryk", 23, listOf(Position.LW, Position.RW), 138, 165),
                PlayerSeed(317, "Nicolas Jackson", 23, listOf(Position.ST), 142, 160),
                PlayerSeed(318, "Christopher Nkunku", 26, listOf(Position.AM, Position.ST), 155, 168),
                PlayerSeed(319, "Armando Broja", 22, listOf(Position.ST), 132, 155),
                PlayerSeed(320, "Noni Madueke", 22, listOf(Position.RW, Position.LW), 138, 158),
            )),
            ClubSeed(4L, "Liverpool", "LIV", listOf(
                PlayerSeed(401, "Alisson Becker", 31, listOf(Position.GK), 165, 175),
                PlayerSeed(402, "Caoimhin Kelleher", 25, listOf(Position.GK), 140, 155),
                PlayerSeed(403, "Virgil van Dijk", 33, listOf(Position.CB), 165, 180),
                PlayerSeed(404, "Ibrahima Konate", 25, listOf(Position.CB), 148, 170),
                PlayerSeed(405, "Joel Matip", 32, listOf(Position.CB), 148, 162),
                PlayerSeed(406, "Jarell Quansah", 21, listOf(Position.CB), 132, 162),
                PlayerSeed(407, "Andrew Robertson", 30, listOf(Position.LB), 158, 168),
                PlayerSeed(408, "Trent Alexander-Arnold", 25, listOf(Position.RB, Position.CM), 162, 175),
                PlayerSeed(409, "Kostas Tsimikas", 28, listOf(Position.LB), 138, 148),
                PlayerSeed(410, "Alexis Mac Allister", 25, listOf(Position.CM, Position.AM), 158, 168),
                PlayerSeed(411, "Dominik Szoboszlai", 23, listOf(Position.CM, Position.AM), 155, 172),
                PlayerSeed(412, "Wataru Endo", 31, listOf(Position.DM, Position.CM), 145, 150),
                PlayerSeed(413, "Curtis Jones", 23, listOf(Position.CM), 140, 158),
                PlayerSeed(414, "Mohamed Salah", 32, listOf(Position.RW, Position.LW), 170, 180),
                PlayerSeed(415, "Luis Diaz", 27, listOf(Position.LW, Position.RW), 155, 168),
                PlayerSeed(416, "Darwin Nunez", 25, listOf(Position.ST, Position.LW), 152, 172),
                PlayerSeed(417, "Diogo Jota", 27, listOf(Position.ST, Position.LW), 155, 165),
                PlayerSeed(418, "Cody Gakpo", 25, listOf(Position.LW, Position.ST), 148, 165),
                PlayerSeed(419, "Harvey Elliott", 21, listOf(Position.RW, Position.CM), 138, 162),
                PlayerSeed(420, "Ryan Gravenberch", 22, listOf(Position.CM), 140, 168),
            )),
            ClubSeed(5L, "Manchester City", "MCI", listOf(
                PlayerSeed(501, "Ederson", 30, listOf(Position.GK), 160, 168),
                PlayerSeed(502, "Stefan Ortega", 31, listOf(Position.GK), 142, 148),
                PlayerSeed(503, "Ruben Dias", 27, listOf(Position.CB), 162, 175),
                PlayerSeed(504, "John Stones", 30, listOf(Position.CB, Position.DM), 155, 168),
                PlayerSeed(505, "Manuel Akanji", 28, listOf(Position.CB), 148, 158),
                PlayerSeed(506, "Nathan Ake", 29, listOf(Position.CB, Position.LB), 148, 158),
                PlayerSeed(507, "Josko Gvardiol", 22, listOf(Position.CB, Position.LB), 155, 180),
                PlayerSeed(508, "Kyle Walker", 34, listOf(Position.RB), 152, 162),
                PlayerSeed(509, "Rico Lewis", 19, listOf(Position.RB, Position.CM), 135, 175),
                PlayerSeed(510, "Rodri", 28, listOf(Position.DM, Position.CM), 170, 178),
                PlayerSeed(511, "Kevin De Bruyne", 33, listOf(Position.AM, Position.CM), 175, 185),
                PlayerSeed(512, "Bernardo Silva", 29, listOf(Position.AM, Position.RW), 162, 170),
                PlayerSeed(513, "Phil Foden", 24, listOf(Position.AM, Position.LW), 162, 178),
                PlayerSeed(514, "Jack Grealish", 28, listOf(Position.LW, Position.AM), 152, 162),
                PlayerSeed(515, "Jeremy Doku", 22, listOf(Position.RW, Position.LW), 148, 172),
                PlayerSeed(516, "Erling Haaland", 23, listOf(Position.ST), 175, 190),
                PlayerSeed(517, "Julian Alvarez", 24, listOf(Position.ST, Position.AM), 155, 172),
                PlayerSeed(518, "Mateo Kovacic", 30, listOf(Position.CM, Position.DM), 148, 158),
                PlayerSeed(519, "Matheus Nunes", 25, listOf(Position.CM), 140, 155),
                PlayerSeed(520, "Oscar Bobb", 20, listOf(Position.RW, Position.AM), 130, 165),
            )),
            ClubSeed(6L, "Tottenham Hotspur", "TOT", listOf(
                PlayerSeed(601, "Guglielmo Vicario", 27, listOf(Position.GK), 145, 155),
                PlayerSeed(602, "Fraser Forster", 36, listOf(Position.GK), 135, 145),
                PlayerSeed(603, "Cristian Romero", 26, listOf(Position.CB), 152, 168),
                PlayerSeed(604, "Micky van de Ven", 23, listOf(Position.CB), 145, 172),
                PlayerSeed(605, "Eric Dier", 30, listOf(Position.CB, Position.DM), 142, 155),
                PlayerSeed(606, "Ben Davies", 31, listOf(Position.CB, Position.LB), 138, 148),
                PlayerSeed(607, "Destiny Udogie", 21, listOf(Position.LB, Position.LW), 142, 168),
                PlayerSeed(608, "Pedro Porro", 24, listOf(Position.RB, Position.RW), 145, 162),
                PlayerSeed(609, "Emerson Royal", 25, listOf(Position.RB), 138, 148),
                PlayerSeed(610, "Yves Bissouma", 27, listOf(Position.DM, Position.CM), 148, 158),
                PlayerSeed(611, "Pape Sarr", 21, listOf(Position.CM), 142, 168),
                PlayerSeed(612, "James Maddison", 27, listOf(Position.AM, Position.CM), 155, 165),
                PlayerSeed(613, "Pierre-Emile Hojbjerg", 28, listOf(Position.DM, Position.CM), 145, 152),
                PlayerSeed(614, "Heung-min Son", 31, listOf(Position.LW, Position.ST), 162, 172),
                PlayerSeed(615, "Dejan Kulusevski", 24, listOf(Position.RW, Position.AM), 148, 162),
                PlayerSeed(616, "Brennan Johnson", 23, listOf(Position.RW, Position.LW), 140, 160),
                PlayerSeed(617, "Richarlison", 27, listOf(Position.ST, Position.LW), 148, 158),
                PlayerSeed(618, "Timo Werner", 28, listOf(Position.ST, Position.LW), 142, 158),
                PlayerSeed(619, "Giovani Lo Celso", 28, listOf(Position.CM, Position.AM), 145, 158),
                PlayerSeed(620, "Oliver Skipp", 23, listOf(Position.DM, Position.CM), 135, 152),
            ))
        )

        private data class ClubSeed(
            val id: Long,
            val name: String,
            val shortName: String,
            val players: List<PlayerSeed>
        )
    }
}
