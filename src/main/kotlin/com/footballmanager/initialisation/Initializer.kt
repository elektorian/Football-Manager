package com.footballmanager.initialisation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.entities.League
import com.footballmanager.seasons.SeasonService
import com.footballmanager.team.ClubsSourceData
import com.footballmanager.team.TeamService
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Component
class Initializer(
    private val mapper: ObjectMapper,
    private val leagues: ConcurrentHashMap<UUID, League>,
    private val seasonService: SeasonService,
    private val teamService: TeamService,
) {
    companion object {
        private const val START_YEAR = 2020
    }
    @PostConstruct
    fun postConstruct() {
        val clubsSourceData: ClubsSourceData = mapper.readValue(ClassPathResource("data/club.json").inputStream)
        val clubs = CopyOnWriteArraySet(clubsSourceData.clubs)
        teamService.add(clubs)
        val league = leagues[clubsSourceData.leagueId]
            ?: throw IllegalStateException("League ${clubsSourceData.leagueId} not found")
        seasonService.create(
            year = START_YEAR,
            clubs = clubs,
            league = league,
        )
    }
}