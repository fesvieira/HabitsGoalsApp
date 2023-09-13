package com.fesvieira.habitsgoals.helpers

import android.content.Context
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

        if (habit.id == 0 || isEnqueued) return

        val data = Data.Builder()
            .putString(HABIT_NAME, habit.name)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(15, TimeUnit.MINUTES)
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