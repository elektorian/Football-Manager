package com.footballmanager.events

import com.footballmanager.notifications.model.Notification
import org.springframework.stereotype.Component
import java.util.concurrent.LinkedBlockingQueue

@Component
class EventsEngine {
    private val events = LinkedBlockingQueue<Notification>()

    fun isEmpty() = events.isEmpty()

    fun process() {

    }
}