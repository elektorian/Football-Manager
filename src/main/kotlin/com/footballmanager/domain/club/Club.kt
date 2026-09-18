package com.footballmanager.domain.club

import java.util.UUID
import java.util.concurrent.CopyOnWriteArraySet

class Club(
    val id: UUID,
    val name: String,
    val tournamentId: UUID?,
    val managerId: UUID?,
    val playerIds: CopyOnWriteArraySet<UUID>
)
