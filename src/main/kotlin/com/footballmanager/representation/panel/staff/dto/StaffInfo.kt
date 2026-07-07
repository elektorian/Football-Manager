package com.footballmanager.representation.panel.staff.dto

data class StaffInfo(
    val chairman: StaffPersonInfo?,
    val director: StaffPersonInfo?,
    val manager: StaffPersonInfo?,
    val assistant: StaffPersonInfo?,
    val scout: StaffPersonInfo?,
    val academy: StaffPersonInfo?,
    val physio: StaffPersonInfo?,
)
