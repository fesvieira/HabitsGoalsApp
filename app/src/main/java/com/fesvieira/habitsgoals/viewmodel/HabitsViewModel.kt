package com.fesvieira.habitsgoals.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {
    var habits by mutableStateOf(emptyList<Habit>())

    private val _selectedHabit = MutableStateFlow(emptyHabit)
    val selectedHabit get() = _selectedHabit

    private val _createOrUpdateHabitError = MutableStateFlow<String?>(null)
    val createOrUpdateHabitError get() = _createOrUpdateHabitError

    fun getHabits() = viewModelScope.launch {
        habitRepository.getHabits().collect { habitsList ->
            habits = habitsList
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
        habitRepository.deleteHabit(habit)
    }

    fun addHabit() = viewModelScope.launch(Dispatchers.IO) {
        habitRepository.addHabit(selectedHabit.value)
    }

    fun updateOrAddHabit(context: Context) = viewModelScope.launch {
        val name = selectedHabit.value.name
        val goal = selectedHabit.value.goal

        _createOrUpdateHabitError.value = when {
            name.isEmpty() || name.isBlank() -> "Invalid name"
            goal <= 0 -> "Invalid goal"
            else -> null
        }
        if (_createOrUpdateHabitError.value == null) {
            if (habits.find { it.name == selectedHabit.value.name } != null) {
                updateHabit()
            } else {
                addHabit()
            }
        }
    }

    fun updateSelectedHabit(name: String) {
        selectedHabit.value = selectedHabit.value.copy(name = name)
    }

    fun updateSelectedHabitGoal(goal: String) {
        if (goal.length > 4 || !goal.isDigitsOnly()) return
        val intGoal = goal.toIntOrNull() ?: return
        selectedHabit.value = selectedHabit.value.copy(goal = intGoal)
    }

    fun updateSelectedHabit(reminder: Boolean) {
        selectedHabit.value = selectedHabit.value.copy(reminder = reminder)
    }
}