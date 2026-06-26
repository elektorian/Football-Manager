package com.footballmanager.config

import com.footballmanager.data.SeedDataGenerator
import com.footballmanager.engine.DayProcessor
import com.footballmanager.engine.GameEngine
import com.footballmanager.engine.GameWorld
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class GameConfig(
    private val seedData: SeedDataGenerator
) {

    @Bean
    fun gameWorld(): GameWorld {
        val world = GameWorld(LocalDate.of(2024, 7, 1))
        seedData.populate(world)
        return world
    }

    @Bean
    fun gameEngine(world: GameWorld, processors: List<DayProcessor>): GameEngine {
        return GameEngine(world, processors)
    }
}
