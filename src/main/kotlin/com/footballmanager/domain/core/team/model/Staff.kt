package com.footballmanager.domain.core.team.model

import com.footballmanager.domain.core.person.enumeration.PersonContractRole
import com.footballmanager.domain.core.person.model.Person
import java.util.UUID

class Staff {
    @Volatile
    var chairman: UUID? = null
    private set
    @Volatile
    var director: UUID? = null
        private set
    @Volatile
    var manager: UUID? = null
        private set
    @Volatile
    var assistant: UUID? = null
        private set
    @Volatile
    var scout: UUID? = null
        private set
    @Volatile
    var physio: UUID? = null
        private set
    @Volatile
    var academy: UUID? = null
        private set

    fun set(person: Person) {
        if (person.contract == null) throw IllegalStateException("Staff could not be initialized.")
        when (val role = person.contract.role) {
            PersonContractRole.CHAIRMAN -> chairman = person.id
            PersonContractRole.SCOUT -> scout = person.id
            PersonContractRole.MANAGER -> manager = person.id
            PersonContractRole.PHYSIO -> physio = person.id
            PersonContractRole.ACADEMY -> academy = person.id
            PersonContractRole.ASSISTANT -> assistant = person.id
            PersonContractRole.DIRECTOR -> director = person.id
            else -> throw IllegalStateException("Staff $role is not valid")
        }
    }
}
