package com.fesvieira.habitsgoals.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.fesvieira.habitsgoals.repository.HabitDao
import com.fesvieira.habitsgoals.repository.HabitDatabase
import com.fesvieira.habitsgoals.repository.HabitRepository
import com.fesvieira.habitsgoals.repository.HabitsRepositoryImpl
import com.fesvieira.habitsgoals.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideHabitDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        HabitDatabase::class.java,
        "HABIT_TABLE"
    ).build()

    @Provides
    fun provideHabitDao(
        habitDb: HabitDatabase
    ) = habitDb.habitDao()

    @Provides
    fun provideHabitRepository(
        habitDao: HabitDao
    ): HabitRepository = HabitsRepositoryImpl(habitDao)

    @Provides
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository = UserPreferencesRepository(context.dataStore)
}