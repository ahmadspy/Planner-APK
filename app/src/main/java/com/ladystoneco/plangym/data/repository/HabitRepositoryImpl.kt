package com.ladystoneco.plangym.data.repository

import com.ladystoneco.plangym.data.local.dao.HabitDao
import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import com.ladystoneco.plangym.domain.repository.HabitRepository
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {

    // Habits
    override fun observeActiveHabits() = habitDao.observeActiveHabits()
    override fun observeHabit(id: Long) = habitDao.observeHabit(id)
    override suspend fun getHabitsWithReminders() = habitDao.getHabitsWithReminders()
    override suspend fun upsertHabit(habit: HabitEntity) = habitDao.upsert(habit)
    override suspend fun setHabitArchived(id: Long, archived: Boolean) =
        habitDao.setArchived(id, archived)
    override suspend fun deleteHabit(habit: HabitEntity) = habitDao.delete(habit)

    // Logs
    override fun observeAllLogs() = habitDao.observeAllLogs()
    override fun observeLogsForHabit(habitId: Long) =
        habitDao.observeLogs(habitId)
    override fun observeAllLogsInRange(start: Long, end: Long) =
        habitDao.observeAllLogsInRange(start, end)
    override suspend fun getLogForDate(habitId: Long, dateEpochDay: Long) =
        habitDao.getLogForDay(habitId, dateEpochDay)
    override suspend fun upsertLog(log: HabitLogEntity) = habitDao.upsertLog(log)
    override suspend fun deleteLog(log: HabitLogEntity) = habitDao.deleteLog(log)
}
