package com.fesvieira.habitsgoals.di

import android.content.Context
import androidx.room.Room
import com.fesvieira.habitsgoals.repository.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.junit.Assert.*
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModuleTest {
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, HabitDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}