package com.footballmanager.presentation.controller

import com.footballmanager.application.dto.NotificationDto
import com.footballmanager.application.port.input.NotificationUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationUseCase: NotificationUseCase,
) {
    @GetMapping
    fun getNotifications(): Collection<NotificationDto> {
        return notificationUseCase.getAll()
    }

    @PostMapping("/{id}")
    fun getNotification(@PathVariable("id") id: UUID) = notificationUseCase.read(id)
}
