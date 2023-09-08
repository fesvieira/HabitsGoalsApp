package com.fesvieira.habitsgoals.ui.screens

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.helpers.FakeDao
import com.fesvieira.habitsgoals.helpers.isAllowedTo
import com.fesvieira.habitsgoals.repository.HabitsRepositoryImpl
import com.fesvieira.habitsgoals.ui.components.AppFloatActionButton
import com.fesvieira.habitsgoals.ui.components.TopBar
import com.fesvieira.habitsgoals.ui.theme.HabitsGoalsTheme
import com.fesvieira.habitsgoals.ui.theme.Typography
import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditCreateHabitScreen(
    navController: NavController,
    habitsViewModel: HabitsViewModel,
    onSetReminder: () -> Unit
) {
    val selectedHabit by habitsViewModel.selectedHabit.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var isReminderActive by remember { mutableStateOf(false) }

    var textName by remember { mutableStateOf(selectedHabit.name) }
    var textGoal by remember {
        mutableStateOf(
            if (selectedHabit.goal == 0) ""
            else selectedHabit.goal.toString()
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        if (allowed) {
            isReminderActive = true
            onSetReminder()
            habitsViewModel.updateOrAddHabit(
                selectedHabit.name,
                selectedHabit.goal.toString(),
                onError = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.only_numbers_allowed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            coroutineScope.launch {
                keyboardController?.hide()
                delay(200)
                navController.popBackStack()
            }
        } else {
            isReminderActive = false
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
                if (isReminderActive) {
                    if (context.isAllowedTo(POST_NOTIFICATIONS) || Build.VERSION.SDK_INT < 33) {
                        onSetReminder()
                    } else {
                        permissionLauncher.launch(POST_NOTIFICATIONS)
                        return@AppFloatActionButton
                    }
                }

                habitsViewModel.updateOrAddHabit(
                    selectedHabit.name,
                    selectedHabit.goal.toString(),
                    onError = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.only_numbers_allowed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                coroutineScope.launch {
                    keyboardController?.hide()
                    delay(200)
                    navController.popBackStack()
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
                value = textName,
                onValueChange = {
                    selectedHabit.name = it
                    textName = it
                },
                label = { Text(stringResource(R.string.habit_name)) }
            )

            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = textGoal,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        selectedHabit.goal = it.toInt()
                    }
                    textGoal = it
                },
                label = { Text(stringResource(R.string.goal)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.how_many_days_do_you),
                fontSize = 13.sp,
                color = Color.Gray
            )

            AnimatedVisibility(visible = selectedHabit.name != "") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Checkbox(
                        checked = isReminderActive,
                        onCheckedChange = {
                            isReminderActive = true
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

@Preview(
    device = "spec:width=1080px,height=2340px,dpi=440",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun PreviewEditHabitsScreen() {
    HabitsGoalsTheme {
        EditCreateHabitScreen(
            navController = rememberNavController(),
            habitsViewModel = HabitsViewModel(
                habitRepository = HabitsRepositoryImpl(
                    habitDao = FakeDao
                )
            ),
            onSetReminder = {}
        )
    }
}