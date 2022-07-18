package com.fesvieira.habitsgoals

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fesvieira.habitsgoals.HabitRepository.habitsList

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "habit-list-screen") {
        composable("habit-list-screen") {
            HabitListScreen(list = habitsList, navController = navController)
        }
        composable("edit-create-habit-screen/{habitName}",
        arguments = listOf(navArgument("habitName") {type = NavType.StringType})) {
            EditCreateHabitScreen(
                it.arguments?.getString("habitName"),
                navController = navController)
        }
    }
}