package com.fesvieira.habitsgoals.helpers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.HABIT_NAME
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.NOTIFICATION_ID
import com.fesvieira.habitsgoals.model.Habit
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object NotificationsService {
    fun scheduleNotification(
        context: Context,
        habit: Habit
    ) {
        val tag = "$NOTIFICATION_ID${habit.id}"
        val instanceWorkManager = WorkManager.getInstance(context)
        val isEnqueued = instanceWorkManager
            .getWorkInfosByTag(tag)
            .get()
            .indexOfFirst { it.state == ENQUEUED } != -1

        if (isEnqueued)  cancelReminder(context, habit.id)

        val data = Data.Builder()
            .putString(HABIT_NAME, habit.name)
            .build()

        val reminder = habit.reminder ?: return
        val localTime = LocalTime.now()
        val reminderTime = LocalTime.of(reminder / 60, reminder % 60)

        val duration = Duration.between(localTime, reminderTime)
        val delay = if (duration.seconds > 0) { duration.seconds } else { 24 * 3600 + duration.seconds }

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .setInputData(data)
                .addTag(tag)
                .build()

        instanceWorkManager.enqueue(periodicWorkRequest)
        Log.d(
            "NotificationsService",
            "Enqueued: $tag, ${habit.name}, " +
                    "delay ${delay / 3600}h${(delay % 3600) / 60}m${(delay % 3600) % 60}s"
        )
    }

    fun cancelReminder(context: Context, habitId: Int) {
        val tag = "$NOTIFICATION_ID${habitId}"
        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.cancelAllWorkByTag(tag)
        Log.d(
            "NotificationsService",
            "Canceled: $tag"
        )
    }
}