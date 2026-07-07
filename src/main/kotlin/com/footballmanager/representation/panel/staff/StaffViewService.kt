package com.footballmanager.representation.panel.staff

import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.dao.PersonRepository
import com.footballmanager.domain.dao.TeamRepository
import com.footballmanager.representation.panel.staff.dto.StaffInfo
import com.footballmanager.representation.panel.staff.dto.StaffPersonInfo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StaffViewService(
    private val teamRepository: TeamRepository,
    private val personRepository: PersonRepository,
) {
    fun getStaff(team: UUID): StaffInfo {
        val staff = teamRepository.get(team).staff
        return StaffInfo(
            chairman = staff.chairman?.let { createStaffPersonInfo(it) },
            manager = staff.manager?.let { createStaffPersonInfo(it) },
            assistant = staff.assistant?.let { createStaffPersonInfo(it) },
            scout = staff.scout?.let { createStaffPersonInfo(it) },
            director = staff.director?.let { createStaffPersonInfo(it) },
            academy = staff.academy?.let { createStaffPersonInfo(it) },
            physio = staff.physio?.let { createStaffPersonInfo(it) },
        )
    }

    private fun createStaffPersonInfo(person: Person): StaffPersonInfo {
        return StaffPersonInfo(
            id = person.id,
            name = person.firstName,
            surname = person.lastName,
        )
    }
}