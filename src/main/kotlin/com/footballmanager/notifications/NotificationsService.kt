package com.footballmanager.notifications

import com.footballmanager.notifications.model.Notification
import org.springframework.stereotype.Service
import java.util.concurrent.LinkedBlockingQueue

@Service
class NotificationsService {
    private val notifications = LinkedBlockingQueue<Notification>()

    fun isEmpty() = notifications.isEmpty()
}
