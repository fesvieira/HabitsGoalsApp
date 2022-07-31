package com.fesvieira.habitsgoals.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var textName by remember { mutableStateOf(selectedHabit.name) }
    var textGoal by remember { mutableStateOf(selectedHabit.name) }

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
                        if (textName != "") {
                            if (selectedHabit.name == "") {
                                habitsViewModel.addHabit(
                                    Habit(id = 0, name = textName, strike = 0, goal = textGoal.toInt())
                                )
                            } else {
                                habitsViewModel.updateHabit(textName)
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
                    value = textName,
                    onValueChange = { entry ->
                        textName = entry
                    },
                    label = { Text("Habit name") }
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = textGoal,
                    onValueChange = { entry ->
                        textGoal = entry
                    },
                    label = { Text("Goal") }
                )
                
                Text(
                    text = "How many days you want to repeat the habit?",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

    }
}