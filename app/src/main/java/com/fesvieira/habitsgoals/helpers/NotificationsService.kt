package com.fesvieira.habitsgoals.helpers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.HABIT_NAME
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.NOTIFICATION_ID
import com.fesvieira.habitsgoals.model.Habit
import java.util.Calendar
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

        if (isEnqueued) {
            cancelReminder(context, habit.id)
        }

        val data = Data.Builder()
            .putString(HABIT_NAME, habit.name)
            .build()

        val calendar = Calendar.getInstance()
        val currentMinuteOfDay =
            (calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE)

        val reminder = habit.reminder ?: return

        val delay = if (currentMinuteOfDay > reminder)
            24 * 60 - currentMinuteOfDay + reminder
        else
            reminder - currentMinuteOfDay

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay.toLong(), TimeUnit.MINUTES)
                .setInputData(data)
                .addTag(tag)
                .build()

        instanceWorkManager.enqueue(periodicWorkRequest)
        Log.d(
            "NotificationsService",
            "Enqueued: $tag, ${habit.name}, delay ${delay / 60}h${delay % 60}m"
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