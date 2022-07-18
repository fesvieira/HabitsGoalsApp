package com.fesvieira.habitsgoals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HabitListScreen(list: List<Habit>, navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items(list) { item ->
            HabitCardAdapter(
                name = item.name,
                info = item.strike,
                onClickListener = {
                    navController.navigate("edit_create_habit_screen")
                }
            )
        }
    }
}