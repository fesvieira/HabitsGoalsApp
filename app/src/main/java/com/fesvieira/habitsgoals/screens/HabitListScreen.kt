package com.fesvieira.habitsgoals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fesvieira.habitsgoals.Habit
import com.fesvieira.habitsgoals.HabitCardAdapter
import com.fesvieira.habitsgoals.HabitsViewModel
import com.fesvieira.habitsgoals.R

@Composable
fun HabitListScreen(
    habitsViewModel: HabitsViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        habitsViewModel.getHabits()
    }
    val list = habitsViewModel.habits

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    habitsViewModel.selectedHabit = Habit(0,"",0)
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
            items(list) { item ->
                HabitCardAdapter(
                    name = item.name,
                    info = item.strike,
                    onClickListener = {
                        habitsViewModel.selectedHabit =
                            Habit(
                                id = item.id,
                                name = item.name,
                                strike = item.strike
                            )
                        navController.navigate("edit-create-habit-screen")
                    }
                )
            }
        }
    }
}