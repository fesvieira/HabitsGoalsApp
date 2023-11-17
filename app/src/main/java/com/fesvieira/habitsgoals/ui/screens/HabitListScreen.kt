package com.fesvieira.habitsgoals.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.fesvieira.habitsgoals.helpers.toStamp
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.navigation.Routes.HabitDetails
import com.fesvieira.habitsgoals.ui.components.AppFloatActionButton
import com.fesvieira.habitsgoals.ui.components.DeleteHabitSnackbar
import com.fesvieira.habitsgoals.ui.components.HabitCard
import com.fesvieira.habitsgoals.ui.components.TopBar
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HabitListScreen(
    habitsViewModel: HabitsViewModel,
    navController: NavController
) {
    val habitsList = habitsViewModel.habits
    val context = LocalContext.current
    var shouldLeaveOnBackPress by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }
    val days = remember {
        (0..2).map { LocalDate.now().minusDays(it.toLong()) }
    }

    BackHandler(!shouldLeaveOnBackPress) {
        coroutineScope.launch {
            shouldLeaveOnBackPress = true
            delay(3500)
            shouldLeaveOnBackPress = false
        }

        Toast.makeText(context, context.getString(R.string.press_back), Toast.LENGTH_LONG).show()
    }

    LaunchedEffect(habitToDelete) {
        habitToDelete?.let { habit ->
            habitsViewModel.deleteHabit(habit)
            val job = launch {
                snackBarHostState.showSnackbar("", duration = SnackbarDuration.Indefinite)
            }
            delay(5000)
            job.cancel()
            habitToDelete = null
        }
    }

    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.habits_list)) },
        floatingActionButton = {
            AppFloatActionButton(icon = painterResource(R.drawable.ic_add)) {
                habitsViewModel.selectedHabit.value = emptyHabit
                navController.navigate(HabitDetails)
            }
        },
        snackbarHost = {
            DeleteHabitSnackbar(
                snackbarHostState = snackBarHostState,
                habitName = habitToDelete?.name ?: ""
            ) {
                habitsViewModel.addHabit()

                habitToDelete?.let { habit ->
                    habit.reminder?.let { reminder ->
                        habitsViewModel.scheduleNotification(habit.id, habit.name, reminder)
                    }
                }
                habitToDelete = null
            }
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            if (habitsList.isEmpty()) {
                emptyHabitUI(this)
            } else {
                items(
                    items = habitsList,
                    key = { it.id },
                ) { item ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == DismissValue.DismissedToStart) {
                                habitToDelete = item
                            }

                            true
                        },
                        positionalThreshold = { 0.3f }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .animateItemPlacement(tween(300)),
                        directions = setOf(DismissDirection.EndToStart),
                        background = { SwipeToDismissDynamicBackground(dismissState) },
                        dismissContent = {
                            HabitCard(
                                habit = item,
                                days = days,
                                onClickListener = {
                                    habitsViewModel.selectedHabit.value = item
                                    navController.navigate(HabitDetails)
                                },
                                onToggleDay = { day ->
                                    habitsViewModel.selectedHabit.value = item
                                    habitsViewModel.toggleDayDone(day.toStamp)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun emptyHabitUI(lazyListScope: LazyListScope) {
    lazyListScope.item {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissDynamicBackground(dismissState: DismissState) {
    val isSwiping by remember(dismissState) {
        derivedStateOf { dismissState.dismissDirection == DismissDirection.EndToStart && dismissState.progress > 0.1f }
    }
    val color by animateColorAsState(
        when {
            isSwiping -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.background
        }, label = "color"
    )

    val scale by animateFloatAsState(
        if (isSwiping) 1f else 0.001f,
        label = "scale"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = Color.White,
            modifier = Modifier.scale(scale)
        )
    }
}