package com.fesvieira.habitsgoals.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.navigation.Routes.HabitDetails
import com.fesvieira.habitsgoals.navigation.Routes.HabitList
import com.fesvieira.habitsgoals.ui.screens.HabitDetailScreen
import com.fesvieira.habitsgoals.ui.screens.HabitListScreen
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel

@Composable
fun MainNavHost(
    habitsViewModel: HabitsViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = HabitList) {
        composable(HabitList) {
            HabitListScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }

        composable(
            route = HabitDetails,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            }) {

            HabitDetailScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }
    }
}