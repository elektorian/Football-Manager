package com.footballmanager.representation.domain.notification

import com.footballmanager.domain.core.notification.NotificationsService
import com.footballmanager.representation.domain.notification.dto.NotificationInfo
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationsService: NotificationsService,
) {
    @GetMapping
    fun getNotifications(): Collection<NotificationInfo> {
        return notificationsService.getAll()
    }

    @PostMapping("/{id}")
    fun getNotification(
        @PathVariable("id") id: UUID,
    ) = notificationsService.read(id)
}