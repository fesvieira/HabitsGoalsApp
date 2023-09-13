package com.fesvieira.habitsgoals.viewmodel

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesvieira.habitsgoals.helpers.NotificationWorker
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.repository.HabitRepository
import com.fesvieira.habitsgoals.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val userPreferencesRepository: UserPreferencesRepository
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

    fun deleteHabit(habit: Habit) = viewModelScope.launch(Dispatchers.IO) {
        val tag = "${NotificationWorker.NOTIFICATION_ID}${habit.id}"
        habitRepository.deleteHabit(habit)
        userPreferencesRepository.removeReminder(tag)
    }

    fun addHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            habitRepository.addHabit(selectedHabit.value)
        }
    }

    fun saveHabit(
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val name = selectedHabit.value.name
            val goal = selectedHabit.value.goal

            when {
                name.isEmpty() || name.isBlank() -> {
                    onError("Invalid name")
                    return@launch
                }

                goal <= 0 -> {
                    onError("Invalid goal")
                    return@launch
                }
            }

            if (habits.value.find { it.name == selectedHabit.value.name } != null) {
                updateHabit()
            } else {
                addHabit()
            }
            delay(200)
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

    fun getHabitByName(name: String): Habit? {
        return _habits.value.firstOrNull { it.name == _selectedHabit.value.name }
    }
}