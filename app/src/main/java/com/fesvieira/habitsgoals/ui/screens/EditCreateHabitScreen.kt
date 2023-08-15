package com.fesvieira.habitsgoals.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.navigation.Routes.HabitList
import com.fesvieira.habitsgoals.ui.components.AppFloatActionButton
import com.fesvieira.habitsgoals.ui.components.TopBar
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel

@Composable
fun EditCreateHabitScreen(
    navController: NavController,
    habitsViewModel: HabitsViewModel
) {
    val selectedHabit = remember { habitsViewModel.selectedHabit }
    val context = LocalContext.current

    var textName by remember { mutableStateOf(selectedHabit.name) }
    var textGoal by remember {
        mutableStateOf(
            if (selectedHabit.goal == 0) ""
            else selectedHabit.goal.toString()
        )
    }

    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.habit_factory)) },
        floatingActionButton = {
            AppFloatActionButton(icon = painterResource(R.drawable.ic_save)) {
                habitsViewModel.updateOrAddHabit(textName, textGoal,
                    onError = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.only_numbers_allowed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
                navController.navigate(HabitList)
            }
        }
    ) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = textName,
                onValueChange = { textName = it },
                label = { Text(stringResource(R.string.habit_name)) }
            )

            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = textGoal,
                onValueChange = { textGoal = it },
                label = { Text(stringResource(R.string.goal)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.how_many_days_do_you),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}