package com.footballmanager.domain.core.tactic.enumeration

import com.footballmanager.domain.core.person.enumeration.PlayerPosition
import com.footballmanager.domain.core.person.enumeration.PlayerPosition.*

enum class TacticSchema(val positions: Map<PlayerPosition, Int>) {
    _4_2_3_1(
        mapOf(
            LD to 1,
            CD to 2,
            RD to 1,
            CM to 2,
            LF to 1,
            CAM to 1,
            RF to 1,
            CF to 1,
        )
    ),
    _4_4_2(
        mapOf(
            LD to 1,
            CD to 2,
            RD to 1,
            CM to 2,
            LM to 1,
            RM to 1,
            CF to 2,
        )
    ),
    _4_3_3(
        mapOf(
            LD to 1,
            CD to 2,
            RD to 1,
            CDM to 1,
            CM to 2,
            LF to 1,
            RF to 1,
            CF to 1,
        )
    ),
    _5_3_2(
        mapOf(
            LD to 1,
            CD to 3,
            RD to 1,
            CM to 3,
            CF to 2,
        )
    ),
    _5_4_1(
        mapOf(
            LD to 1,
            CD to 3,
            RD to 1,
            CM to 2,
            LM to 1,
            RM to 1,
            CF to 1,
        )
    ),
}