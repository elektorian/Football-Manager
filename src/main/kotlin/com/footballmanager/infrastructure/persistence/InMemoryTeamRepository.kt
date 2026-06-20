package com.footballmanager.infrastructure.persistence

import com.footballmanager.domain.model.Team
import com.footballmanager.domain.repository.TeamRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryTeamRepository : TeamRepository {
    private val store = ConcurrentHashMap<UUID, Team>()

    override fun findById(id: UUID): Team? = store[id]

    override fun findAll(): List<Team> = store.values.toList()

    override fun save(team: Team) {
        store[team.id] = team
    }
}
