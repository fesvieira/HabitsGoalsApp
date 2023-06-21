package com.fesvieira.habitsgoals.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.navigation.Routes.EditHabit
import com.fesvieira.habitsgoals.navigation.Routes.HabitList
import com.fesvieira.habitsgoals.navigation.Routes.Splash
import com.fesvieira.habitsgoals.screens.EditCreateHabitScreen
import com.fesvieira.habitsgoals.screens.HabitListScreen
import com.fesvieira.habitsgoals.screens.SplashScreen
import com.fesvieira.habitsgoals.ui.theme.Blue700
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val habitsViewModel = hiltViewModel<HabitsViewModel>()

    val systemUiController = rememberSystemUiController()


    NavHost(navController = navController, startDestination = Splash) {
        composable(Splash) {
            systemUiController.setSystemBarsColor(Color.Black)
            SplashScreen(navController = navController)
        }

        composable(HabitList) {
            systemUiController.setSystemBarsColor(Blue700)
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