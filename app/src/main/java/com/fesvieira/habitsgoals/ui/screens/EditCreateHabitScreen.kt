package com.fesvieira.habitsgoals.ui.screens

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.helpers.isAllowedTo
import com.fesvieira.habitsgoals.ui.components.AppFloatActionButton
import com.fesvieira.habitsgoals.ui.components.TopBar
import com.fesvieira.habitsgoals.ui.theme.Typography
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCreateHabitScreen(
    navController: NavController,
    habitsViewModel: HabitsViewModel
) {
    val selectedHabit by habitsViewModel.selectedHabit.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var shouldSaveHabit by remember { mutableStateOf(false) }

    LaunchedEffect(shouldSaveHabit) {
        if (!shouldSaveHabit) return@LaunchedEffect
        habitsViewModel.saveHabit(
            context = context,
            onError = { error ->
                coroutineScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
                return@saveHabit
            },
            onSuccess = {
                coroutineScope.launch {
                    keyboardController?.hide()
                    delay(200)
                    navController.popBackStack()
                }
            }
        )
        shouldSaveHabit = false
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        if (allowed) {
            habitsViewModel.updateSelectedHabit(reminder = true)
            shouldSaveHabit = true
        } else {
            habitsViewModel.updateSelectedHabit(reminder = false)
            Toast.makeText(
                context,
                context.getString(R.string.unable_to_set_a_reminder_without_notification_permission),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = { TopBar(title = stringResource(R.string.habit_factory)) },
        floatingActionButton = {
            AppFloatActionButton(icon = painterResource(R.drawable.ic_save)) {
                if (selectedHabit.reminder) {
                    if (context.isAllowedTo(POST_NOTIFICATIONS) || Build.VERSION.SDK_INT < 33) {
                        shouldSaveHabit = true
                    } else {
                        permissionLauncher.launch(POST_NOTIFICATIONS)
                    }
                } else {
                    shouldSaveHabit = true
                }
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
                value = selectedHabit.name,
                onValueChange = { habitsViewModel.updateSelectedHabit(name = it) },
                label = { Text(stringResource(R.string.habit_name)) }
            )

            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = if (selectedHabit.goal == 0) "" else selectedHabit.goal.toString(),
                onValueChange = { habitsViewModel.updateSelectedHabitGoal(goal = it) },
                label = { Text(stringResource(R.string.goal)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                text = stringResource(R.string.how_many_days_do_you),
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        habitsViewModel.updateSelectedHabit(reminder = !selectedHabit.reminder)
                    }
            )

            AnimatedVisibility(visible = selectedHabit.name != "") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Checkbox(
                        checked = selectedHabit.reminder,
                        onCheckedChange = {
                            habitsViewModel.updateSelectedHabit(reminder = !selectedHabit.reminder)
                        }
                    )

                    Text(
                        text = stringResource(R.string.remind_me_about_this_habit),
                        style = Typography.body2
                    )
                }
            }
        }
    }
}