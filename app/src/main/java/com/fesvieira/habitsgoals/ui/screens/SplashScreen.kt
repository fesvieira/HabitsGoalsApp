package com.fesvieira.habitsgoals.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.navigation.Routes
import com.fesvieira.habitsgoals.ui.theme.md_theme_light_primary
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    habitsViewModel: HabitsViewModel,
    navController: NavController = rememberNavController()
) {
    val isLoading by habitsViewModel.areHabitsLoading.collectAsState()
    var playAnimation by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(targetValue = if (playAnimation)  1.0f else 0.1f,
        label = "scaleAnim"
    )

    LaunchedEffect(Unit) {
        playAnimation = true
    }

    LaunchedEffect(isLoading) {
        if (isLoading) return@LaunchedEffect
        delay(500)
        navController.navigate(Routes.HabitList)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(240.dp)
                .scale(scaleAnim)
                .padding(64.dp)
                .background(md_theme_light_primary, RoundedCornerShape(32.dp))
        )
    }
}