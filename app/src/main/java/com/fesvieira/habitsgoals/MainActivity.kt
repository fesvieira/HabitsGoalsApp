package com.fesvieira.habitsgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.fesvieira.habitsgoals.navigation.MainNavHost
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitsGoalsTheme {
                MainNavHost()
            }
        }
    }
}


