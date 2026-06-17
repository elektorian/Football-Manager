package com.footballmanager.notifications

import com.footballmanager.notifications.dto.NotificationInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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