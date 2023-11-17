package com.fesvieira.habitsgoals.ui.screens

 import android.Manifest.permission.POST_NOTIFICATIONS
 import android.os.Build
 import android.widget.Toast
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.result.contract.ActivityResultContracts
 import androidx.compose.animation.AnimatedContent
 import androidx.compose.animation.AnimatedVisibility
 import androidx.compose.animation.fadeIn
 import androidx.compose.animation.fadeOut
 import androidx.compose.animation.slideInVertically
 import androidx.compose.animation.slideOutVertically
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Row
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.foundation.text.KeyboardOptions
 import androidx.compose.material3.Checkbox
 import androidx.compose.material3.CheckboxDefaults
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.OutlinedTextField
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.Text
 import androidx.compose.material3.rememberTimePickerState
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.runtime.collectAsState
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.rememberCoroutineScope
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.ExperimentalComposeUiApi
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.draw.clip
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.platform.LocalSoftwareKeyboardController
 import androidx.compose.ui.res.painterResource
 import androidx.compose.ui.res.stringResource
 import androidx.compose.ui.text.input.KeyboardCapitalization
 import androidx.compose.ui.text.input.KeyboardType
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
 import androidx.navigation.NavController
 import com.fesvieira.habitsgoals.R
 import com.fesvieira.habitsgoals.helpers.habit.longToTime
 import com.fesvieira.habitsgoals.helpers.isAllowedTo
 import com.fesvieira.habitsgoals.helpers.noRippleClickable
 import com.fesvieira.habitsgoals.ui.components.AppFloatActionButton
 import com.fesvieira.habitsgoals.ui.components.TopBar
 import com.fesvieira.habitsgoals.ui.components.calendar.CalendarComponent
 import com.fesvieira.habitsgoals.ui.components.dialogs.CalendarDialog
 import com.fesvieira.habitsgoals.ui.components.dialogs.TimerPickerDialog
 import com.fesvieira.habitsgoals.ui.theme.Typography
 import com.fesvieira.habitsgoals.viewmodel.HabitsViewModel
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.delay
 import kotlinx.coroutines.launch
 import java.time.LocalDate
 import java.time.LocalDateTime
 import java.time.LocalTime
 import java.time.OffsetDateTime

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    navController: NavController,
    habitsViewModel: HabitsViewModel
) {
    val selectedHabit by habitsViewModel.selectedHabit.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var shouldSaveHabit by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    val date by remember { mutableStateOf(LocalDate.now()) }
    val selectedHabitDaysDone = habitsViewModel.selectedHabitDaysDone
    var showCalendar by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        habitsViewModel.refreshDoneDays()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        if (allowed) {
            shouldSaveHabit = true
        } else {
            habitsViewModel.updateSelectedHabit(reminder = null)
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
                if (selectedHabit.reminder != null) {
                    if (context.isAllowedTo(POST_NOTIFICATIONS) || Build.VERSION.SDK_INT < 33) {
                        shouldSaveHabit = true
                    } else {
                        permissionLauncher.launch(POST_NOTIFICATIONS)
                    }
                } else {
                    shouldSaveHabit = true
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                OutlinedTextField(
                    value = selectedHabit.name,
                    onValueChange = { habitsViewModel.updateSelectedHabit(name = it) },
                    label = { Text(stringResource(R.string.habit_name)) },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }

            item {
                OutlinedTextField(
                    value = if (selectedHabit.goal == 0) "" else selectedHabit.goal.toString(),
                    onValueChange = { habitsViewModel.updateSelectedHabitGoal(goal = it) },
                    label = { Text(stringResource(R.string.goal)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }

            item {
                Text(
                    text = stringResource(R.string.how_many_days_do_you),
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .noRippleClickable {
                            if (selectedHabit.reminder == null) {
                                showTimePicker = true
                            } else {
                                habitsViewModel.updateSelectedHabit(null)
                            }
                        }
                        .padding(start = 8.dp, end = 24.dp)
                ) {
                    Checkbox(
                        checked = selectedHabit.reminder != null,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.tertiary
                        ),
                        onCheckedChange = {
                            if (selectedHabit.reminder == null) {
                                showTimePicker = true
                            } else {
                                habitsViewModel.updateSelectedHabit(null)
                            }
                        }
                    )

                    AnimatedContent(
                        targetState = if (selectedHabit.reminder == null) stringResource(R.string.remind_me_about_this_habit)
                        else "Reminder for: ${selectedHabit.reminder?.longToTime}",
                        label = "",
                    ) {
                        Text(
                            text = it,
                            style = Typography.bodyMedium
                        )
                    }
                }
            }
            item {
                Text(text = "Show Calendar", style = Typography.bodyMedium, modifier = Modifier.clickable { showCalendar = true })
            }
        }

        AnimatedVisibility(
            visible = showCalendar,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            CalendarDialog(
                baseDate = date,
                daysDone = selectedHabitDaysDone,
                onToggleDay = { habitsViewModel.toggleDayDone(it) },
                onDismiss = { showCalendar = false }
            )
        }

        AnimatedVisibility(showTimePicker) {
            TimerPickerDialog(
                timePickerState,
                onDismiss = {
                    showTimePicker = false
                },
                onOkayClick = {
                    showTimePicker = false
                    val timeDate = LocalDateTime.now()
                        .with(LocalTime.MIN)
                        .plusHours(timePickerState.hour.toLong())
                        .plusMinutes(timePickerState.minute.toLong())

                    habitsViewModel.updateSelectedHabit(
                        reminder = timeDate.toEpochSecond(OffsetDateTime.now().offset) * 1000,
                    )
                },
            )
        }
    }
}