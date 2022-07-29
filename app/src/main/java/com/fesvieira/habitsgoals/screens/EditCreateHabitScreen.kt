package com.fesvieira.habitsgoals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fesvieira.habitsgoals.Habit
import com.fesvieira.habitsgoals.HabitsViewModel
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.Blue700
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@Composable
fun EditCreateHabitScreen(
    navController: NavController,
    habitsViewModel: HabitsViewModel
) {
    val selectedHabit = habitsViewModel.selectedHabit

    var text by remember { mutableStateOf(selectedHabit.name) }

    HabitsGoalsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = Blue700,
                ) {
                    Text(
                        text = "Habit Factory",
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (text != "") {
                            if (selectedHabit.name == "") {
                                habitsViewModel.addHabit(
                                    Habit(id = 0, name = text, strike = 0)
                                )
                            } else {
                                habitsViewModel.updateHabit(text)
                            }
                        }
                        navController.navigate("habit-list-screen")
                    },
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_save,
                        ),
                        tint = Color.White,
                        contentDescription = null,
                    )
                }
            }) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = text,
                    onValueChange = { entry ->
                        text = entry
                    },
                    label = { Text("Habit name") }
                )
            }
        }

    }
}