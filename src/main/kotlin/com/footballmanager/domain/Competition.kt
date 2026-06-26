package com.footballmanager.domain

sealed interface Competition {
    val id: Long
    val name: String
    val season: Int
    val clubIds: List<Long>
    val fixtures: MutableList<Fixture>
}

data class League(
    override val id: Long,
    override val name: String,
    override val season: Int,
    override val clubIds: List<Long>,
    override val fixtures: MutableList<Fixture> = mutableListOf(),
    val standings: MutableList<Standing> = mutableListOf()
) : Competition

data class Cup(
    override val id: Long,
    override val name: String,
    override val season: Int,
    override val clubIds: List<Long>,
    override val fixtures: MutableList<Fixture> = mutableListOf(),
    val rounds: List<CupRound> = emptyList()
) : Competition

data class CupRound(
    val name: String,
    val fixtureIds: List<Long>
)
