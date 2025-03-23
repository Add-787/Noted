package com.psyluckco.noted.data.firebase

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsService @Inject constructor(
    private val analytics: FirebaseAnalytics
)  : AnalyticsService {

    override fun logTaskCompletedEvent(taskId: String) {

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, taskId)
        }

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)
        Timber.i("Logging task (id $taskId) as completed.")

    }

    override fun logTaskAddedEvent() {
        analytics.logEvent(FirebaseAnalytics.Event.GENERATE_LEAD, null)
        Timber.i("Logging adding new task event")
    }

    override fun logTaskUpdatedEvent(taskId: String) {
        TODO("Not yet implemented")
    }
}