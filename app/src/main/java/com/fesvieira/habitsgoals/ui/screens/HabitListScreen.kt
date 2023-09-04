package com.fesvieira.habitsgoals.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.navigation.Routes.EditHabit
import com.fesvieira.habitsgoals.ui.components.AppDialog
import com.fesvieira.habitsgoals.ui.components.AppFloatActionButton
import com.fesvieira.habitsgoals.ui.components.HabitCard
import com.fesvieira.habitsgoals.ui.components.TopBar
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitListScreen(
    habitsViewModel: HabitsViewModel,
    navController: NavController
) {
    val list = habitsViewModel.habits
    val context = LocalContext.current
    var shouldLeaveOnBackPress by remember { mutableStateOf(false)}
    val coroutineScope = rememberCoroutineScope()
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }

    BackHandler(!shouldLeaveOnBackPress) {
        coroutineScope.launch {
            shouldLeaveOnBackPress = true
            delay(3500)
            shouldLeaveOnBackPress = false
        }

        Toast.makeText(context, "Press back again to leave", Toast.LENGTH_LONG).show()
    }

    LaunchedEffect(Unit) {
        habitsViewModel.getHabits()
    }

    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.habits_list)) },
        floatingActionButton = {
            AppFloatActionButton(icon = painterResource(R.drawable.ic_add)) {
                habitsViewModel.selectedHabit = emptyHabit
                navController.navigate(EditHabit)
            }
        }) { paddingValues ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            if (list.isEmpty()) {
                item {
                    Column {
                        Text(
                            text = stringResource(R.string.add_your_habits_to_start),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 40.dp, start = 40.dp, end = 40.dp)
                        )

                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(R.raw.rocket)
                        )
                        val logoAnimationState =
                            animateLottieCompositionAsState(
                                composition = composition,
                                iterations = LottieConstants.IterateForever
                            )

                        LottieAnimation(
                            composition = composition,
                            progress = { logoAnimationState.progress },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
                                .size(250.dp)
                        )
                    }
                }
            }

            items(
                items = list,
                key = { it.hashCode() },
            ) { item ->
                val dismissState = rememberDismissState(
                    confirmStateChange = { dismissValue ->
                        if (dismissValue == DismissValue.DismissedToStart) {
                            habitToDelete = item
                        }
                        false
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp),
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.1f) },
                    background = { SwipeToDismissDynamicBackground(dismissState) },
                    dismissContent = {
                        HabitCard(
                            habit = item,
                            onClickListener = {
                                habitsViewModel.selectedHabit = item
                                navController.navigate(EditHabit)
                            },
                            onAddClickListener = {
                                habitsViewModel.selectedHabit = item
                                habitsViewModel.addStrike()
                            }
                        )
                    }
                )
            }
        }
    }

    if (habitToDelete != null) {
        AppDialog(
            yesCallback = {
                habitsViewModel.deleteHabit(habitToDelete ?: return@AppDialog)
                habitToDelete = null
            },
            noCallback = { habitToDelete = null },
            habitName = habitToDelete?.name ?: "",
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeToDismissDynamicBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> MaterialTheme.colors.background
            else -> MaterialTheme.colors.error
        }, label = "color"
    )
    val alignment = Alignment.CenterEnd
    val icon = Icons.Default.Delete

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
        label = "scale"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = stringResource(R.string.delete_icon),
            tint = Color.White,
            modifier = Modifier.scale(scale)
        )
    }
}