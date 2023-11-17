package com.fesvieira.habitsgoals.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.alarmmanager.AlarmItem
import com.fesvieira.habitsgoals.alarmmanager.AndroidAlarmScheduler
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val alarmScheduler: AndroidAlarmScheduler
) : ViewModel() {
    private var _habits = mutableStateListOf<Habit>()
    val habits: List<Habit> = _habits

    private val _selectedHabit = MutableStateFlow(emptyHabit)
    val selectedHabit get() = _selectedHabit

    private val _selectedHabitDaysDone = SnapshotStateList<Long>()
    var selectedHabitDaysDone: List<Long> = _selectedHabitDaysDone

    private val _areHabitsLoading = MutableStateFlow<Boolean>(false)
    val areHabitsLoading get() = _areHabitsLoading.asStateFlow()

    init {
        _areHabitsLoading.value = true
        viewModelScope.launch(Dispatchers.Main) {
            habitRepository.getHabits().collect { habitList ->
                _habits.clear()
                _habits.addAll(habitList)
                _areHabitsLoading.value = false
            }
        }
    }

    private fun updateHabit() = viewModelScope.launch(Dispatchers.IO) {
        habitRepository.updateHabit(selectedHabit.value)
    }

    fun toggleDayDone(dayStamp: Long) = viewModelScope.launch(Dispatchers.Main) {
        val newSelectedHabit = _selectedHabit.value
        val daysDone = newSelectedHabit.daysDone.toMutableList()

        if (daysDone.contains(dayStamp)) daysDone.remove(dayStamp)
        else daysDone.add(dayStamp)

        newSelectedHabit.daysDone = daysDone
        _selectedHabit.value = newSelectedHabit

        _selectedHabitDaysDone.clear()
        _selectedHabitDaysDone.addAll(daysDone)

        updateHabit()
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmScheduler.cancel(habit.id)
            habitRepository.deleteHabit(habit)
        }
    }

    fun addHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.addHabit(selectedHabit.value)
        }
    }

    fun saveHabit(
        context: Context,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val name = selectedHabit.value.name
            val goal = selectedHabit.value.goal

            when {
                name.isEmpty() || name.isBlank() -> {
                    onError(context.getString(R.string.invalid_name))
                    return@launch
                }

                goal <= 0 -> {
                    onError(context.getString(R.string.invalid_goal))
                    return@launch
                }
            }

            if (getHabitById() != null) updateHabit()
            else addHabit()

            this.launch {
                delay(1000)

                getHabitByName()?.let {
                    val reminder = it.reminder
                    if (reminder != null) {
                        scheduleNotification(it.id, it.name, reminder)
                    } else {
                        alarmScheduler.cancel(it.id)
                    }
                }
            }

            onSuccess()
        }
    }

    fun updateSelectedHabit(name: String) {
        _selectedHabit.value = selectedHabit.value.copy(name = name)
    }

    fun updateSelectedHabitGoal(goal: String) {
        if (goal.length > 4 || !goal.isDigitsOnly()) return
        val intGoal = goal.toIntOrNull() ?: return
        _selectedHabit.value = selectedHabit.value.copy(goal = intGoal)
    }

    fun updateSelectedHabit(reminder: Long?) {
        _selectedHabit.value = selectedHabit.value.copy(reminder = reminder)
    }

    /** Fetch habit from database with Room given id */
    private fun getHabitByName(): Habit? {
        return _habits.firstOrNull { it.name == _selectedHabit.value.name }
    }

    private fun getHabitById(): Habit? {
        return _habits.firstOrNull { it.id == _selectedHabit.value.id }
    }

    fun refreshDoneDays() {
        _selectedHabitDaysDone.clear()
        _selectedHabitDaysDone.addAll(_selectedHabit.value.daysDone)
    }

    fun scheduleNotification(habitId: Int, habitName: String, reminder: Long) {
        alarmScheduler.schedule(item = AlarmItem(
            habitId = habitId,
            habitName = habitName,
            time = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(reminder),
                ZoneId.systemDefault()
            )
        ))
    }
}