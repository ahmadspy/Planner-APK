package com.ladystoneco.plangym.data.local.dao

import androidx.room.*
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtaskDao {

    @Query("SELECT * FROM subtasks WHERE taskId = :taskId ORDER BY orderIndex ASC")
    fun observeSubtasks(taskId: Long): Flow<List<SubtaskEntity>>

    @Upsert
    suspend fun upsert(subtask: SubtaskEntity)

    @Update
    suspend fun update(subtask: SubtaskEntity)

    @Query("UPDATE subtasks SET isDone = :done WHERE id = :id")
    suspend fun setDone(id: Long, done: Boolean)

    @Delete
    suspend fun delete(subtask: SubtaskEntity)
}
