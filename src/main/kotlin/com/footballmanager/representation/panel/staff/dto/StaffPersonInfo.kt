package com.footballmanager.representation.panel.staff.dto

import java.util.UUID

data class StaffPersonInfo(
    val name: String,
    val surname: String,
    val id: UUID,
)
