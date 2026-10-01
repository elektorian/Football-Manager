package com.footballmanager.model.player

import com.footballmanager.model.player.entity.Contract
import com.footballmanager.model.player.entity.Player
import com.footballmanager.model.player.repository.ContractRepository
import com.footballmanager.model.player.repository.PlayerRepository
import com.footballmanager.model.tournament.entity.Tournament
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
    private val contractRepository: ContractRepository,
) {
    fun createPlayer(
        id: UUID,
        firstName: String,
        lastName: String,
        contract: Contract,
    ): Player {
        val player = Player(
            id = id,
            firstName = firstName,
            lastName = lastName,
            contract = contract.id,
        )
        contractRepository.addContract(contract)
        playerRepository.addPlayer(player)
        return player
    }
}