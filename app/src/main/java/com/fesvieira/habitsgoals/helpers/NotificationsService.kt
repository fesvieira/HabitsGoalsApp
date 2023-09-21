package com.fesvieira.habitsgoals.helpers

import android.content.Context
import androidx.compose.runtime.currentRecomposeScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.HABIT_ID
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.HABIT_NAME
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.IS_FIRST
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.NOTIFICATION_ID
import com.fesvieira.habitsgoals.model.Habit
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours

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

        if (habit.id == 0 || isEnqueued) return

        val data = Data.Builder()
            .putString(HABIT_NAME, habit.name)
            .build()

        val currentMinuteOfDay = (Calendar.HOUR_OF_DAY * 60) + Calendar.MINUTE

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

        val dataFirst = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_NAME, habit.name)
            .putBoolean(IS_FIRST, true)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest
            .Builder(NotificationWorker::class.java)
            .setInputData(dataFirst)
            .build()

        instanceWorkManager.enqueue(oneTimeWorkRequest)
        instanceWorkManager.enqueue(periodicWorkRequest)
    }

    fun cancelReminder(context: Context, habitId: Int) {
        val tag = "$NOTIFICATION_ID${habitId}"
        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.cancelAllWorkByTag(tag)
    }
}