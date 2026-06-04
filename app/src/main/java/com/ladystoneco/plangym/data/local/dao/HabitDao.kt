package com.ladystoneco.plangym.data.local.dao

import androidx.room.*
import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun observeActiveHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun observeHabit(id: Long): Flow<HabitEntity?>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabit(id: Long): HabitEntity?

    @Query("SELECT * FROM habits WHERE reminderAt IS NOT NULL AND isArchived = 0")
    suspend fun getHabitsWithReminders(): List<HabitEntity>

    @Upsert
    suspend fun upsert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("UPDATE habits SET isArchived = :archived WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean)

    @Delete
    suspend fun delete(habit: HabitEntity)

    // ---- Logs ----

    @Query("SELECT * FROM habit_logs")
    fun observeAllLogs(): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY dateEpochDay DESC")
    fun observeLogs(habitId: Long): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE dateEpochDay BETWEEN :start AND :end ORDER BY dateEpochDay ASC")
    fun observeAllLogsInRange(start: Long, end: Long): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND dateEpochDay = :date LIMIT 1")
    suspend fun getLogForDay(habitId: Long, date: Long): HabitLogEntity?

    @Upsert
    suspend fun upsertLog(log: HabitLogEntity)

    @Delete
    suspend fun deleteLog(log: HabitLogEntity)
}
