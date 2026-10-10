package com.footballmanager.messages.controller

import com.footballmanager.messages.MessageService
import com.footballmanager.messages.entity.Message
import com.footballmanager.messages.entity.matchOverview.MatchOverviewMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/messages")
class MessageController(
    private val messageService: MessageService,
) {
    @GetMapping
    fun getMessages(): List<Message> {
        return messageService.getAll()
    }

    @GetMapping("/match-overview/{id}")
    fun getMatchOverview(@PathVariable id: UUID): MatchOverviewMessage {
        return messageService.read(id)
    }
}