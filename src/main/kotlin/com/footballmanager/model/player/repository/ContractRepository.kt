package com.footballmanager.model.player.repository

import com.footballmanager.model.player.entity.Contract
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class ContractRepository {
    private val contracts = ConcurrentHashMap<UUID, Contract>()

    fun addContract(contract: Contract) {
        contracts[contract.id] = contract
    }
}