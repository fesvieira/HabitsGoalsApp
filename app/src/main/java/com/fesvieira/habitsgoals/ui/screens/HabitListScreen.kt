package com.fesvieira.habitsgoals.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.ui.components.HabitCard
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.navigation.Routes.EditHabit
import com.fesvieira.habitsgoals.ui.theme.Blue700
import com.fesvieira.habitsgoals.ui.theme.black

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitListScreen(
    habitsViewModel: HabitsViewModel,
    navController: NavController
) {
    val list = habitsViewModel.habits

    BackHandler {}

    LaunchedEffect(Unit) {
        habitsViewModel.getHabits()
    }

    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = {
            NewHabitButton {
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
                            progress = {
                                logoAnimationState.progress
                            },
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
                key = { listItem ->
                    listItem.hashCode()
                },
            ) { item ->
                val dismissState = rememberDismissState()

                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    habitsViewModel.deleteHabit(item)
                }

                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp),
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = { FractionalThreshold(0.1f) },
                    background = { SwipeToDismissDynamicBackground(dismissState) },
                    dismissContent = {
                        HabitCard(
                            name = item.name,
                            info = item.strike,
                            goal = item.goal,
                            onClickListener = {
                                habitsViewModel.selectedHabit =
                                    Habit(
                                        id = item.id,
                                        name = item.name,
                                        strike = item.strike,
                                        goal = item.goal
                                    )
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
}

@Composable
private fun TopBar() {
    TopAppBar(
        backgroundColor = Blue700,
    ) {
        Text(
            text = stringResource(R.string.habits_list),
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp),
            letterSpacing = 2.sp
        )
    }
}

@Composable
fun NewHabitButton(onClick: () -> Unit) {
    FloatingActionButton(onClick) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            tint = Color.White,
            contentDescription = stringResource(R.string.add_icon),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeToDismissDynamicBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> black
            else -> Color.Red
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
            modifier = Modifier.scale(scale)
        )
    }
}