package com.fesvieira.habitsgoals.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotificationWorker
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.HABIT_ID
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.HABIT_NAME
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.IS_FIRST
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.NOTIFICATION_ID
import com.fesvieira.habitsgoals.helpers.NotificationWorker.Companion.WORK_NAME
import com.fesvieira.habitsgoals.model.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(): ViewModel() {

    fun scheduleNotification(
        context: Context,
        habit: Habit
    ) {
        val data = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_NAME, habit.name)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(15, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag("$NOTIFICATION_ID${habit.id}")
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

        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.enqueue(oneTimeWorkRequest)
        instanceWorkManager.enqueue(periodicWorkRequest)

    }

    fun cancelReminder(context: Context, habitId: Int) {
        viewModelScope.launch {
            WorkManager.getInstance(context).cancelAllWorkByTag("$NOTIFICATION_ID${habitId}")
        }
    }
}