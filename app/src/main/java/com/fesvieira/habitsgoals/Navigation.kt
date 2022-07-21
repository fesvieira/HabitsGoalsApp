package com.fesvieira.habitsgoals

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fesvieira.habitsgoals.screens.EditCreateHabitScreen
import com.fesvieira.habitsgoals.screens.HabitListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "habit-list-screen") {
        composable("habit-list-screen") {
            HabitListScreen(navController = navController)
        }

        composable("edit-create-habit-screen") {
            EditCreateHabitScreen(
                navController = navController)
        }
    }
}