package com.fesvieira.habitsgoals.viewmodel

import android.content.Context
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
    private var _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits get() = _habits

    private val _selectedHabit = MutableStateFlow(emptyHabit)
    val selectedHabit get() = _selectedHabit

    private val _notifyCanceledReminder = MutableStateFlow<String?>(null)
    val notifyCancelledReminder: StateFlow<String?> get() = _notifyCanceledReminder

    init {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.getHabits().collect { habitList ->
                _habits.value = habitList
            }
        }
    }

    private fun updateHabit() = viewModelScope.launch(Dispatchers.IO) {
        habitRepository.updateHabit(selectedHabit.value)
    }

    fun addStrike() = viewModelScope.launch(Dispatchers.IO) {
        val newStrike = selectedHabit.value.strike + 1
        _selectedHabit.value = selectedHabit.value.copy(strike = newStrike)
        habitRepository.updateHabit(selectedHabit.value)
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
            }
            else {
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
        return _habits.value.firstOrNull { it.name == _selectedHabit.value.name }
    }

    fun setCancelledReminder(value: String?) {
        _notifyCanceledReminder.value = value
    }
}