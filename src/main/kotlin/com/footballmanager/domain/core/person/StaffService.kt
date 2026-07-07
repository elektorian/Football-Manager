package com.footballmanager.domain.core.person

import com.footballmanager.domain.core.person.model.Person
import com.footballmanager.domain.dao.PersonRepository
import com.footballmanager.domain.dao.TeamRepository
import org.springframework.stereotype.Service

@Service
class StaffService(
    private val teamRepository: TeamRepository,
    private val personRepository: PersonRepository,
) {
    fun register(staff: Person) {
        personRepository.merge(staff)
        if (staff.contract == null) return
        val team = teamRepository.get(staff.contract.team)
        team.staff.set(staff)
    }
}