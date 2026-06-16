package com.footballmanager.team

import com.footballmanager.entities.Club
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TeamService {
    private val teams = ConcurrentHashMap<UUID, Club>() // todo make bean, rework jsons and setup

    fun getTeam(id: UUID): Club = teams[id]!!

    fun add(teams: Collection<Club>) {
        teams.forEach { this.teams[it.id] = it }
    }
}