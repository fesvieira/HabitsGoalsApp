package com.fesvieira.habitsgoals

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.HabitRepository.habitsList

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "habit_list_screen") {
        composable("habit_list_screen") {
            HabitListScreen(list = habitsList, navController = navController)
        }
        composable("edit_create_habit_screen") { EditCreateHabitScreen() }
    }
}