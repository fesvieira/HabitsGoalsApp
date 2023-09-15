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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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
            NotificationsService.cancelReminder(context, habit.id)
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

            if (habits.value.find { it.name == selectedHabit.value.name } != null) {
                updateHabit()
            } else {
                addHabit()
            }

            delay(200)
            getHabitByName()?.let { habit ->
                if (_selectedHabit.value.reminder) {
                    NotificationsService.scheduleNotification(context, habit)
                } else {
                    NotificationsService.cancelReminder(context, habit.id)
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

    fun updateSelectedHabit(reminder: Boolean) {
        _selectedHabit.value = selectedHabit.value.copy(reminder = reminder)
    }

    private fun getHabitByName(): Habit? {
        return _habits.value.firstOrNull { it.name == _selectedHabit.value.name }
    }
}