package com.fesvieira.habitsgoals

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fesvieira.habitsgoals.helpers.NotifyWorker
import com.fesvieira.habitsgoals.helpers.NotifyWorker.Companion.NOTIFICATION_ID
import com.fesvieira.habitsgoals.helpers.NotifyWorker.Companion.WORK_NAME
import com.fesvieira.habitsgoals.navigation.MainNavHost
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

private fun scheduleNotification(
    context: Context,
    delay: Long,
    data: Data
) {
    val request =
        PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(WORK_NAME)
            .build()

    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.enqueue(request)

    CoroutineScope(Dispatchers.Default).launch {
        delay(3000)
        instanceWorkManager.cancelAllWorkByTag(WORK_NAME)
        Log.d("Aoba", instanceWorkManager.getWorkInfosByTag(WORK_NAME).toString())
    }
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
