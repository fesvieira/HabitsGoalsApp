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
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val userPreferences: UserPreferencesRepository
): ViewModel() {

    private val _reminders = MutableStateFlow<Set<String>>(emptySet())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.reminders.collect { newList ->
                _reminders.value = newList
            }
        }
    }

    fun scheduleNotification(
        context: Context,
        habit: Habit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val tag = "$NOTIFICATION_ID${habit.id}"
            println(_reminders)
            if (_reminders.value.contains(tag)) return@launch

            val instanceWorkManager = WorkManager.getInstance(context)

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
            userPreferences.addReminder(tag)
        }
    }

    fun cancelReminder(context: Context, habitId: Int) {
        viewModelScope.launch {
            val tag = "$NOTIFICATION_ID${habitId}"
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
            userPreferences.removeReminder(tag)
        }
    }
}