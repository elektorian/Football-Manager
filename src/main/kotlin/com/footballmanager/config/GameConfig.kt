package com.footballmanager.config

import com.footballmanager.engine.DayProcessor
import com.footballmanager.engine.GameEngine
import com.footballmanager.engine.GameWorld
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class GameConfig {

    @Bean
    fun gameWorld(): GameWorld {
        return GameWorld(LocalDate.of(2024, 7, 1))
    }

    @Bean
    fun gameEngine(world: GameWorld, processors: List<DayProcessor>): GameEngine {
        return GameEngine(world, processors)
    }
}
