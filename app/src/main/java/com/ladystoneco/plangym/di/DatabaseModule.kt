package com.ladystoneco.plangym.di

import android.content.Context
import androidx.room.Room
import com.ladystoneco.plangym.data.local.AppDatabase
import com.ladystoneco.plangym.data.local.dao.HabitDao
import com.ladystoneco.plangym.data.local.dao.ProjectDao
import com.ladystoneco.plangym.data.local.dao.SubtaskDao
import com.ladystoneco.plangym.data.local.dao.TaskDao
import com.ladystoneco.plangym.data.local.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "plangym.db"
    ).build()

    @Provides
    fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideSubtaskDao(db: AppDatabase): SubtaskDao = db.subtaskDao()

    @Provides
    fun provideHabitDao(db: AppDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideWorkoutDao(db: AppDatabase): WorkoutDao = db.workoutDao()
}
