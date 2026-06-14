package com.footballmanager.application

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.footballmanager.domain.model.GameMeta
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * SaveLoadService handles the persistence of game states to the local filesystem.
 * 
 * Business Requirements:
 * - Data is stored in a modular directory structure (/saves/{saveName}/...).
 * - Each save consists of multiple files (currently only meta.json is implemented).
 * - Uses Jackson with JavaTimeModule for JSON serialization of LocalDate.
 */
@Service
class SaveLoadService {
    private val mapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
    private val savesDir = Paths.get(System.getProperty("user.home"), "Documents", "FootballManager", "saves").toString()

    init {
        Files.createDirectories(Paths.get(savesDir))
    }

    /**
     * Saves the current meta state to a folder named after [name].
     */
    fun saveGame(name: String, meta: GameMeta) {
        val folder = File("$savesDir/$name")
        if (!folder.exists()) folder.mkdirs()
        
        val file = File("${folder.absolutePath}/meta.json")
        mapper.writeValue(file, meta)
    }

    /**
     * Loads the game meta from a folder named after [name].
     * Returns null if the save file does not exist.
     */
    fun loadGame(name: String): GameMeta? {
        val file = File("$savesDir/$name/meta.json")
        return if (file.exists()) {
            mapper.readValue<GameMeta>(file)
        } else null
    }
}
