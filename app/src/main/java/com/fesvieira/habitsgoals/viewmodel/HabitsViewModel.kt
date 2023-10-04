package com.fesvieira.habitsgoals.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesvieira.habitsgoals.R
import com.fesvieira.habitsgoals.helpers.NotificationsService
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {
    private var _habits = mutableStateListOf<Habit>()
    val habits: List<Habit> = _habits

    private val _selectedHabit = MutableStateFlow(emptyHabit)
    val selectedHabit get() = _selectedHabit

    private val _selectedHabitDaysDone = SnapshotStateList<Long>()
    var selectedHabitDaysDone: List<Long> = _selectedHabitDaysDone

    private val _notifyCanceledReminder = MutableStateFlow<String?>(null)
    val notifyCancelledReminder: StateFlow<String?> get() = _notifyCanceledReminder

    init {
        _habits.clear()
        _selectedHabitDaysDone.clear()
        viewModelScope.launch(Dispatchers.Main) {
            habitRepository.getHabits().collect { habitList ->
                _habits.clear()
                _habits.addAll(habitList)
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

    fun deleteHabit(habit: Habit, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.deleteHabit(habit)
            NotificationsService.cancelReminder(context, habit.name) {}
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

            if (getHabitByName() != null) {
                updateHabit()
            } else {
                addHabit()
            }

            if (_selectedHabit.value.reminder != null) {
                NotificationsService.scheduleNotification(context, _selectedHabit.value)
            } else {
                NotificationsService.cancelReminder(context, _selectedHabit.value.name) {
                    setCancelledReminder(_selectedHabit.value.name)
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

    fun updateSelectedHabit(reminder: Int?) {
        _selectedHabit.value = selectedHabit.value.copy(reminder = reminder)
    }

    private fun getHabitByName(): Habit? {
        return _habits.firstOrNull { it.name == _selectedHabit.value.name }
    }

    fun setCancelledReminder(value: String?) {
        _notifyCanceledReminder.value = value
    }

    fun refreshDoneDays() {
        _selectedHabitDaysDone.clear()
        _selectedHabitDaysDone.addAll(_selectedHabit.value.daysDone)
    }
}