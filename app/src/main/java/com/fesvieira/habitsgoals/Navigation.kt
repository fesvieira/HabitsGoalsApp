package com.fesvieira.habitsgoals

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.screens.EditCreateHabitScreen
import com.fesvieira.habitsgoals.screens.HabitListScreen
import com.fesvieira.habitsgoals.screens.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val habitsViewModel = hiltViewModel<HabitsViewModel>()

    NavHost(navController = navController, startDestination = "splash-screen") {
        composable("splash-screen") {
            SplashScreen(navController = navController)
        }

        composable("habit-list-screen") {
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