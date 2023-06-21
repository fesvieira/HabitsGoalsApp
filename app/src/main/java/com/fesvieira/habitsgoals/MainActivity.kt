package com.fesvieira.habitsgoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fesvieira.habitsgoals.navigation.MainNavHost
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
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
