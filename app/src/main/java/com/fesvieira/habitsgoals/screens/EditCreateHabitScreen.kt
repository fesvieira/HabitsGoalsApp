package com.fesvieira.habitsgoals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fesvieira.habitsgoals.Habit
import com.fesvieira.habitsgoals.HabitsViewModel
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@Composable
fun EditCreateHabitScreen(
    habitName: String? = null,
    navController: NavController,
    habitsViewModel: HabitsViewModel = hiltViewModel()
) {

    var text by remember { mutableStateOf(habitName ?: "") }
    val selectedHabit = habitsViewModel.selectedHabit

    HabitsGoalsTheme {
        Scaffold(floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    habitsViewModel.addHabit(
                        Habit(name = text, strike = 0)
                    )
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

        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { entry ->
                    text = entry
                },
                label = { Text(if (selectedHabit.id != 0) selectedHabit.name else "") }
            )
        }
    }
}

@Composable
@Preview
fun PreviewEditText() {

}