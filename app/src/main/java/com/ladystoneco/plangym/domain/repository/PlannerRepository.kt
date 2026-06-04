package com.ladystoneco.plangym.domain.repository

import com.ladystoneco.plangym.data.local.entity.ProjectEntity
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.data.local.relation.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {

    // Projects
    fun observeAllProjects(): Flow<List<ProjectEntity>>
    fun observeActiveProjects(): Flow<List<ProjectEntity>>
    fun observeProject(id: Long): Flow<ProjectEntity?>
    suspend fun upsertProject(project: ProjectEntity)
    suspend fun setProjectArchived(id: Long, archived: Boolean)
    suspend fun deleteProject(project: ProjectEntity)

    // Tasks
    fun observeAllTasks(): Flow<List<TaskEntity>>
    fun observeTasks(projectId: Long): Flow<List<TaskEntity>>
    fun observeTasksWithSubtasks(projectId: Long): Flow<List<TaskWithSubtasks>>
    fun observeTasksByProject(projectId: Long): Flow<List<TaskEntity>>
    fun observeOpenTasks(): Flow<List<TaskEntity>>
    fun observeTasksInRange(start: Long, end: Long): Flow<List<TaskEntity>>
    fun observeTask(id: Long): Flow<TaskEntity?>
    suspend fun getTasksWithReminders(): List<TaskEntity>
    suspend fun upsertTask(task: TaskEntity)
    suspend fun setTaskDone(id: Long, done: Boolean, completedAt: Long? = null)
    suspend fun deleteTask(task: TaskEntity)

    // Subtasks
    fun observeSubtasks(taskId: Long): Flow<List<SubtaskEntity>>
    suspend fun upsertSubtask(subtask: SubtaskEntity)
    suspend fun setSubtaskDone(id: Long, done: Boolean)
    suspend fun deleteSubtask(subtask: SubtaskEntity)
}
