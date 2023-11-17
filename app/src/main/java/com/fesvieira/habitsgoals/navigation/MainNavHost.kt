package com.fesvieira.habitsgoals.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.navigation.Routes.HabitDetails
import com.fesvieira.habitsgoals.navigation.Routes.HabitList
import com.fesvieira.habitsgoals.navigation.Routes.Splash
import com.fesvieira.habitsgoals.ui.screens.HabitDetailScreen
import com.fesvieira.habitsgoals.ui.screens.HabitListScreen
import com.fesvieira.habitsgoals.ui.screens.SplashScreen
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel

@Composable
fun MainNavHost(
    habitsViewModel: HabitsViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Splash) {
        composable(Splash) {
            SplashScreen(
                habitsViewModel = habitsViewModel,
                navController = navController
            )
        }

        composable(
            route = HabitList,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }
        ) {
            HabitListScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }

        composable(
            route = HabitDetails,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300, easing = LinearEasing)) +
                        slideOutOfContainer(animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {

            HabitDetailScreen(
                navController = navController,
                habitsViewModel = habitsViewModel
            )
        }
    }
}