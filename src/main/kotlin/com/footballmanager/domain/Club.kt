package com.footballmanager.domain

data class Club(
    val id: Long,
    val name: String,
    val shortName: String,
    val balance: Long = 0,
    val wageBudget: Long = 0,
    val players: MutableList<Person> = mutableListOf(),
    val staff: MutableList<Person> = mutableListOf(),
    val competitionIds: MutableList<Long> = mutableListOf()
)
