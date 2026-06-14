package com.footballmanager.application

import com.footballmanager.domain.model.GameMeta
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SaveLoadServiceTest {
    private val service = SaveLoadService()

    @Test
    fun `saveGame and loadGame should persist GameMeta correctly`() {
        val saveName = "test_save_${System.currentTimeMillis()}"
        val meta = GameMeta(currentDate = LocalDate.of(2025, 1, 1), gameName = "Persistence Test")

        service.saveGame(saveName, meta)
        val loaded = service.loadGame(saveName)

        assertNotNull(loaded)
        assertEquals("Persistence Test", loaded?.gameName)
        assertEquals(LocalDate.of(2025, 1, 1), loaded?.currentDate)
    }

    @Test
    fun `loadGame should return null for non-existent save`() {
        val loaded = service.loadGame("non_existent_save")
        assertNull(loaded)
    }
}
