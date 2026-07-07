package com.footballmanager.domain.core.events

import com.footballmanager.domain.core.notification.model.Notification
import org.springframework.stereotype.Component
import java.util.concurrent.LinkedBlockingQueue

@Component
class EventsEngine {
    private val events = LinkedBlockingQueue<Notification>()

    fun isEmpty() = events.isEmpty()

    fun process() {

    }
}