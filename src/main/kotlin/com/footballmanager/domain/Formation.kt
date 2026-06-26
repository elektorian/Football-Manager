package com.footballmanager.domain

data class Formation(
    val name: String,
    val slots: List<Position>
) {
    companion object {
        val DEFAULT = Formation(
            name = "4-4-2",
            slots = listOf(
                Position.GK,
                Position.LB, Position.CB, Position.CB, Position.RB,
                Position.LM, Position.CM, Position.CM, Position.RM,
                Position.ST, Position.ST
            )
        )
    }
}
