package com.footballmanager.domain.dao.inMemory

import com.footballmanager.domain.core.team.model.Team
import com.footballmanager.domain.dao.TeamRepository
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Repository
class TeamInMemoryRepository(
    private val teams: ConcurrentHashMap<UUID, Team>,
) : TeamRepository{
    override fun get(id: UUID): Team = teams[id]!!
    override fun findAll(): List<Team> {
        return teams.values.toList()
    }
}
