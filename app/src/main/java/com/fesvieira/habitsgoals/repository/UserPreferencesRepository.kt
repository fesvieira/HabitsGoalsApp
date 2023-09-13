package com.fesvieira.habitsgoals.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val REMINDERS = stringSetPreferencesKey("reminders")
    }

    val reminders: Flow<Set<String>> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            preferences[REMINDERS] ?: emptySet()
        }

    suspend fun addReminder(reminder: String) {
        val newReminders = reminders.first().toMutableSet()
        newReminders.add(reminder)
        dataStore.edit { preferences ->
            preferences[REMINDERS] = newReminders
        }
    }

    suspend fun removeReminder(reminder: String) {
        val newReminders = reminders.first().toMutableSet()
        newReminders.removeIf { it == reminder }
        dataStore.edit { preferences ->
            preferences[REMINDERS] = newReminders
        }
    }
}