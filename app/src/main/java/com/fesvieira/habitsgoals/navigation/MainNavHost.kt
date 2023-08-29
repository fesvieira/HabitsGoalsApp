package com.fesvieira.habitsgoals.navigation

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.navigation.Routes.EditHabit
import com.fesvieira.habitsgoals.navigation.Routes.HabitList
import com.fesvieira.habitsgoals.ui.screens.EditCreateHabitScreen
import com.fesvieira.habitsgoals.ui.screens.HabitListScreen
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val habitsViewModel = hiltViewModel<HabitsViewModel>()

    val systemUiController = rememberSystemUiController()

    NavHost(navController = navController, startDestination = HabitList) {
        composable(HabitList) {
            systemUiController.setSystemBarsColor(MaterialTheme.colors.primary)
            HabitListScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }

        composable(EditHabit) {
            EditCreateHabitScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }
    }
}