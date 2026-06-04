package com.ladystoneco.plangym.di

import com.ladystoneco.plangym.data.repository.HabitRepositoryImpl
import com.ladystoneco.plangym.data.repository.PlannerRepositoryImpl
import com.ladystoneco.plangym.data.repository.WorkoutRepositoryImpl
import com.ladystoneco.plangym.domain.repository.HabitRepository
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import com.ladystoneco.plangym.domain.repository.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlannerRepository(
        impl: PlannerRepositoryImpl
    ): PlannerRepository

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        impl: HabitRepositoryImpl
    ): HabitRepository

    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        impl: WorkoutRepositoryImpl
    ): WorkoutRepository
}
