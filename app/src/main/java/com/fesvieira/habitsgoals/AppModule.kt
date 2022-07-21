package com.fesvieira.habitsgoals

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
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
}