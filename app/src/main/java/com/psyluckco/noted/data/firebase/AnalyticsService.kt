package com.psyluckco.noted.data.firebase

interface AnalyticsService {
    fun logTaskCompletedEvent(taskId: String)
    fun logTaskAddedEvent()
    fun logTaskUpdatedEvent(taskId: String)
}