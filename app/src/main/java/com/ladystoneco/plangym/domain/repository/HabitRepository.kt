package com.ladystoneco.plangym.domain.repository

import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    // Habits
    fun observeActiveHabits(): Flow<List<HabitEntity>>
    fun observeHabit(id: Long): Flow<HabitEntity?>
    suspend fun getHabitsWithReminders(): List<HabitEntity>
    suspend fun upsertHabit(habit: HabitEntity)
    suspend fun setHabitArchived(id: Long, archived: Boolean)
    suspend fun deleteHabit(habit: HabitEntity)

    // Logs
    fun observeAllLogs(): Flow<List<HabitLogEntity>>
    fun observeLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>
    fun observeAllLogsInRange(start: Long, end: Long): Flow<List<HabitLogEntity>>
    suspend fun getLogForDate(habitId: Long, dateEpochDay: Long): HabitLogEntity?
    suspend fun upsertLog(log: HabitLogEntity)
    suspend fun deleteLog(log: HabitLogEntity)
}
