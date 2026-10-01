package com.footballmanager.model.match.repository

import com.footballmanager.model.club.entity.Club
import com.footballmanager.model.match.entity.Match
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class MatchRepository {
    private val matches = ConcurrentHashMap<UUID, com.footballmanager.model.match.entity.Match>()
}