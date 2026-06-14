package com.footballmanager.domain.model

import java.time.LocalDate

/**
 * GameMeta represents the core state of a game session.
 * 
 * Business Requirements:
 * - The game always starts on July 1st, 2020.
 * - This object is intended to be serialized as 'meta.json' within a save folder.
 */
data class GameMeta(
    var currentDate: LocalDate = LocalDate.of(2020, 7, 1),
    var gameName: String = "New World"
)
