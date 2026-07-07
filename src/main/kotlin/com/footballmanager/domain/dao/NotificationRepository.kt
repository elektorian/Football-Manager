package com.footballmanager.domain.dao

import com.footballmanager.domain.core.notification.model.Notification
import java.util.UUID

interface NotificationRepository {
    fun save(notification: Notification)
    fun findAll(): List<Notification>
    fun count(): Int
    fun isEmpty(): Boolean
    fun get(id: UUID): Notification
    fun delete(id: UUID)
}