package com.fesvieira.habitsgoals.di

import android.content.Context
import androidx.room.Room
import com.fesvieira.habitsgoals.alarmmanager.AndroidAlarmScheduler
import com.fesvieira.habitsgoals.model.DateStampsConverter
import com.fesvieira.habitsgoals.repository.HabitDao
import com.fesvieira.habitsgoals.repository.HabitDatabase
import com.fesvieira.habitsgoals.repository.HabitRepository
import com.fesvieira.habitsgoals.repository.HabitsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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
    )
        .addTypeConverter(DateStampsConverter())
        .build()

    @Provides
    fun provideHabitDao(
        habitDb: HabitDatabase
    ) = habitDb.habitDao()

    @Provides
    fun provideHabitRepository(
        habitDao: HabitDao
    ): HabitRepository = HabitsRepositoryImpl(habitDao)

    @Provides
    fun provideAndroidAlarmScheduler(
        @ApplicationContext
        context: Context
    ): AndroidAlarmScheduler = AndroidAlarmScheduler(context)
}