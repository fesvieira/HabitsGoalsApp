package com.fesvieira.habitsgoals

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotifyWorker
import com.fesvieira.habitsgoals.helpers.NotifyWorker.Companion.NOTIFICATION_ID
import com.fesvieira.habitsgoals.helpers.NotifyWorker.Companion.WORK_NAME
import com.fesvieira.habitsgoals.navigation.MainNavHost
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }

        val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()

        scheduleNotification(
            context = applicationContext,
            delay = 1000,
            data = data
        )
    }
}

private fun scheduleNotification(context: Context, delay: Long, data: Data) {
    val notificationWork = OneTimeWorkRequest
        .Builder(NotifyWorker::class.java)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(data)
        .build()

    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager
        .beginUniqueWork(WORK_NAME, REPLACE, notificationWork)
        .enqueue()
}

@Composable
fun MainScreen() {
    HabitsGoalsTheme {
        MainNavHost()
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun DefaultPreview() {
    MainScreen()
}
