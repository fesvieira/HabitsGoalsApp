package com.fesvieira.habitsgoals.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotifyWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(): ViewModel() {

    fun scheduleNotification(
        context: Context
    ) {
        val data = Data.Builder()
            .putInt(NotifyWorker.NOTIFICATION_ID, 0)
            .putString(NotifyWorker.HABIT_NAME, "Aoba")
            .build()

        val request =
            PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(1000, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(NotifyWorker.WORK_NAME)
                .build()

        val instanceWorkManager = WorkManager.getInstance(context)
        instanceWorkManager.enqueue(request)

        viewModelScope.launch {
            delay(3000)
            instanceWorkManager.cancelAllWorkByTag(NotifyWorker.WORK_NAME)
        }
    }
}