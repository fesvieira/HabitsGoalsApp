package com.fesvieira.habitsgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.fesvieira.habitsgoals.navigation.MainNavHost
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import com.fesvieira.habitsgoals.viewmodel.NotificationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val notificationsViewModel: NotificationsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitsGoalsTheme {
                MainNavHost()
            }
        }

        notificationsViewModel.scheduleNotification(
            context = applicationContext
        )
    }
}


