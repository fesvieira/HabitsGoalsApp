package com.fesvieira.habitsgoals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme

@Composable
fun EditCreateHabitScreen(habitName: String? = null, navController: NavController) {
    var text by remember{ mutableStateOf(habitName ?: "")}
    HabitsGoalsTheme {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = text,
                onValueChange = { entry ->
                    text = entry
                },
                label = {Text("Habit Name")}
            )
        }
    }
}

@Composable
@Preview
fun PreviewEditText() {

}