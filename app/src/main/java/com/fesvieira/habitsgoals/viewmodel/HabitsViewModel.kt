package com.fesvieira.habitsgoals.viewmodel

import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesvieira.habitsgoals.model.Habit
import com.fesvieira.habitsgoals.model.Habit.Companion.emptyHabit
import com.fesvieira.habitsgoals.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {
    var habits by mutableStateOf(emptyList<Habit>())
    var selectedHabit by mutableStateOf(emptyHabit)

    fun getHabits() = viewModelScope.launch {
        habitRepository.getHabits().collect { habitsList ->
            habits = habitsList
        }
    }

    private fun updateHabit(newName: String, newGoal: Int) = viewModelScope.launch(Dispatchers.IO) {
        selectedHabit = selectedHabit.copy(name = newName, goal = newGoal)
        habitRepository.updateHabit(selectedHabit)
    }

    fun addStrike() = viewModelScope.launch(Dispatchers.IO) {
        val newStrike = selectedHabit.strike + 1
        selectedHabit = selectedHabit.copy(strike = newStrike)
        habitRepository.updateHabit(selectedHabit)
    }

    fun deleteHabit(habit: Habit) = viewModelScope.launch(Dispatchers.IO) {
        habitRepository.deleteHabit(habit)
    }

    fun addHabit(habit: Habit) = viewModelScope.launch(Dispatchers.IO) {
        habitRepository.addHabit(habit)
    }

    fun updateOrAddHabit(
        textName: String,
        textGoal: String,
        onError: () -> Unit
    ) = viewModelScope.launch {
        if (textName.isNotEmpty() && textGoal.isNotEmpty()) {
            if (TextUtils.isDigitsOnly(textGoal)) {
                if (selectedHabit == emptyHabit) {
                    addHabit(Habit(0, textName, 0, textGoal.toInt()))
                } else {
                    updateHabit(textName, textGoal.toInt())
                }
            } else {
                onError()
            }
        }
    }
}