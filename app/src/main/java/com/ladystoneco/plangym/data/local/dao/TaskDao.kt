package com.ladystoneco.plangym.data.local.dao

import androidx.room.*
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.data.local.relation.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY dueAt ASC")
    fun observeAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY isDone ASC, id DESC")
    fun observeTasks(projectId: Long): Flow<List<TaskEntity>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY isDone ASC, id DESC")
    fun observeTasksWithSubtasks(projectId: Long): Flow<List<TaskWithSubtasks>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY dueAt ASC")
    fun observeTasksByProject(projectId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isDone = 0 ORDER BY dueAt ASC")
    fun observeOpenTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueAt BETWEEN :start AND :end ORDER BY dueAt ASC")
    fun observeTasksInRange(start: Long, end: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeTask(id: Long): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE reminderAt IS NOT NULL AND isDone = 0")
    suspend fun getTasksWithReminders(): List<TaskEntity>

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("UPDATE tasks SET isDone = :done, completedAt = :completedAt WHERE id = :id")
    suspend fun setDone(id: Long, done: Boolean, completedAt: Long?)

    @Delete
    suspend fun delete(task: TaskEntity)
}
