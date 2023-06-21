package com.fesvieira.habitsgoals.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.navigation.Routes.HabitList

@Composable
fun SplashScreen(navController: NavHostController) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.splash)
    )
    val logoAnimationState =
        animateLottieCompositionAsState(composition = composition)

    LottieAnimation(
        composition = composition,
        progress = {
            logoAnimationState.progress
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    )

    if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
        navController.navigate(HabitList)
    }
}