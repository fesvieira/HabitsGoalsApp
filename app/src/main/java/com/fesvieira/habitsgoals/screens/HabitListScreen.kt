package com.fesvieira.habitsgoals.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.HabitCardAdapter
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.Blue700
import com.fesvieira.habitsgoals.ui.theme.black

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitListScreen(
    habitsViewModel: HabitsViewModel,
    navController: NavController
) {

    BackHandler {}

    LaunchedEffect(Unit) {
        habitsViewModel.getHabits()
    }
    val list = habitsViewModel.habits

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Blue700,
            ) {
                Text(
                    text = "Habits List",
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp),
                    letterSpacing = 2.sp
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    habitsViewModel.selectedHabit = Habit(0, "", 0, 0)
                    navController.navigate("edit-create-habit-screen")
                },

                ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_add,
                    ),
                    tint = Color.White,
                    contentDescription = null,
                )
            }
        }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (list.isEmpty()) {
                item {
                    Column {
                        Text(
                            text = "Add your Habits to start your enhancement journey",
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
                    modifier = Modifier
                        .padding(vertical = 1.dp),
                    directions = setOf(
                        DismissDirection.EndToStart
                    ),
                    dismissThresholds = { direction ->
                        FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                    },
                    background = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.Default -> black
                                else -> Color.Red
                            }
                        )
                        val alignment = Alignment.CenterEnd
                        val icon = Icons.Default.Delete

                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
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
                                contentDescription = "Delete Icon",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    },
                    dismissContent = {
                        HabitCardAdapter(
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
                                navController.navigate("edit-create-habit-screen")
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