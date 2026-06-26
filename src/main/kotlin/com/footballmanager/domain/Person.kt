package com.footballmanager.domain

data class Person(
    val id: Long,
    val name: String,
    val age: Int,
    val nationality: String,
    val role: PersonRole,
    val positions: List<Position>,
    val ability: Int,
    val potential: Int,
    val contract: Contract?,
    val injured: Boolean = false,
    val injuryWeeks: Int = 0
) {
    init {
        require(ability in 0..200) { "ability must be 0-200" }
        require(potential in 0..200) { "potential must be 0-200" }
        require(ability <= potential) { "ability cannot exceed potential" }
        require(age in 15..70) { "age must be 15-70" }
        if (injured) require(injuryWeeks > 0) { "injuryWeeks must be > 0 when injured" }
    }
}
