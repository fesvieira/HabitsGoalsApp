package com.fesvieira.habitsgoals

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.screens.EditCreateHabitScreen
import com.fesvieira.habitsgoals.screens.HabitListScreen
import com.fesvieira.habitsgoals.screens.SplashScreen
import com.fesvieira.habitsgoals.ui.theme.Blue700
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val habitsViewModel = hiltViewModel<HabitsViewModel>()

    val systemUiController = rememberSystemUiController()


    NavHost(navController = navController, startDestination = "splash-screen") {
        composable("splash-screen") {
            systemUiController.setSystemBarsColor(Color.Black)
            SplashScreen(navController = navController)
        }

        composable("habit-list-screen") {
            systemUiController.setSystemBarsColor(Blue700)
            HabitListScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }

        composable("edit-create-habit-screen") {
            EditCreateHabitScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }
    }
}