package com.footballmanager.domain.repository

import com.footballmanager.domain.model.Notification
import java.util.UUID

interface NotificationRepository {
    fun findAll(): List<Notification>
    fun findById(id: UUID): Notification?
    fun save(notification: Notification)
    fun deleteOldestChecked(count: Int)
}
