package com.footballmanager.setup

import com.footballmanager.model.club.ClubService
import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.player.PlayerService
import com.footballmanager.model.player.entity.Contract
import com.footballmanager.model.player.entity.Player
import com.footballmanager.model.season.SeasonService
import com.footballmanager.model.season.entity.Season
import com.footballmanager.model.tournament.TournamentService
import com.footballmanager.model.tournament.entity.Tournament
import com.footballmanager.model.tournament.repository.TournamentRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet

@Component
class FirstSeasonDataGenerator(
    private val tournamentService: TournamentService,
    private val seasonService: SeasonService,
    private val clubService: ClubService,
    private val playerService: PlayerService,
) {
    @PostConstruct
    fun setup() {
        val rpl = createLeague()
        val rplClubs = createClubs()
        createSeason(
            tournament = rpl,
            clubs = rplClubs,
        )
    }

    private fun createLeague(): Tournament {
        val tournament = tournamentService.createTournament(
            id = UUID.fromString("de124c17-15e1-4e72-80a9-c4fb22d8b5f9"),
            name = "Russian Premier League",
            size = 16,
        )
        return tournament
    }

    private fun createClubs(): Collection<Club> {
        val zenit = createClub("Zenit")
        val spartak = createClub("Spartak Mosckow")
        val krasnodar = createClub("Krasnodar")
        val dinamo = createClub("Dinamo Moscow")
        val rostov = createClub("Rostov")
        val rubin = createClub("Rubin")
        val arsenal = createClub("Arsenal Tula")
        val sochi = createClub("Sochi")
        val terek = createClub("Terek")
        val ufa = createClub("Ufa")
        val ural = createClub("Ural")
        val tambov = createClub("Tambov")
        val lokomotive = createClub("Lokomotiv Moscow")
        val kryljaSovetov = createClub("Krylja Sovetov")
        val cska = createClub("CSKA")
        val khimki = createClub("Khimki")
        return listOf(
            zenit,
            spartak,
            krasnodar,
            dinamo,
            rostov,
            sochi,
            terek,
            rubin,
            kryljaSovetov,
            khimki,
            cska,
            arsenal,
            ufa,
            ural,
            tambov,
            lokomotive,
        )
    }

    private fun createClub(name: String): Club {
        val clubId = UUID.randomUUID()
        val players = (1..20).mapTo(CopyOnWriteArraySet()) { generatePlayer(clubId).id }
        val club = clubService.createClub(
            id = clubId,
            name = name,
            players = players
        )
        return club
    }

    private fun createSeason(
        tournament: Tournament,
        clubs: Collection<Club>,
    ): Season {
        val season = seasonService.createSeason(
            year = 2019,
            clubs = clubs.map { it.id },
            tournament = tournament.id,
        )
        tournament.seasons.add(season.id)
        return season
    }

    private fun generatePlayer(club: UUID): Player {
        val playerId = UUID.randomUUID()
        val contract = Contract(
            id = UUID.randomUUID(),
            player = playerId,
            club = club,
        )
        val player = playerService.createPlayer(
            id = UUID.randomUUID(),
            contract = contract,
            firstName = "Ivan",
            lastName = "Ivanov",
        )
        return player
    }
}