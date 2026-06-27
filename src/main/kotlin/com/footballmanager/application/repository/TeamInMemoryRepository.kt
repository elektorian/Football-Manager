package com.footballmanager.application.repository

import com.footballmanager.domain.repository.TeamRepository
import com.footballmanager.entities.Team
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class TeamInMemoryRepository(
    private val teams: ConcurrentHashMap<UUID, Team>,
) : TeamRepository{
    override fun get(id: UUID): Team  = teams[id]!!
    override fun findAll(): List<Team> {
        return teams.values.toList()
    }
}
