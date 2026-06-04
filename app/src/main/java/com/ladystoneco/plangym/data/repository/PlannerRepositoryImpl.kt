package com.ladystoneco.plangym.data.repository

import com.ladystoneco.plangym.data.local.dao.ProjectDao
import com.ladystoneco.plangym.data.local.dao.SubtaskDao
import com.ladystoneco.plangym.data.local.dao.TaskDao
import com.ladystoneco.plangym.data.local.entity.ProjectEntity
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import javax.inject.Inject

class PlannerRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val subtaskDao: SubtaskDao
) : PlannerRepository {

    // Projects
    override fun observeAllProjects() = projectDao.observeAllProjects()
    override fun observeActiveProjects() = projectDao.observeActiveProjects()
    override fun observeProject(id: Long) = projectDao.observeProject(id)
    override suspend fun upsertProject(project: ProjectEntity) = projectDao.upsert(project)
    override suspend fun setProjectArchived(id: Long, archived: Boolean) =
        projectDao.setArchived(id, archived)
    override suspend fun deleteProject(project: ProjectEntity) = projectDao.delete(project)

    // Tasks
    override fun observeAllTasks() = taskDao.observeAllTasks()
    override fun observeTasks(projectId: Long) = taskDao.observeTasks(projectId)
    override fun observeTasksWithSubtasks(projectId: Long) = taskDao.observeTasksWithSubtasks(projectId)
    override fun observeTasksByProject(projectId: Long) =
        taskDao.observeTasksByProject(projectId)
    override fun observeOpenTasks() = taskDao.observeOpenTasks()
    override fun observeTasksInRange(start: Long, end: Long) =
        taskDao.observeTasksInRange(start, end)
    override fun observeTask(id: Long) = taskDao.observeTask(id)
    override suspend fun getTasksWithReminders() = taskDao.getTasksWithReminders()
    override suspend fun upsertTask(task: TaskEntity) = taskDao.upsert(task)
    override suspend fun setTaskDone(id: Long, done: Boolean, completedAt: Long?) =
        taskDao.setDone(id, done, completedAt)
    override suspend fun deleteTask(task: TaskEntity) = taskDao.delete(task)

    // Subtasks
    override fun observeSubtasks(taskId: Long) = subtaskDao.observeSubtasks(taskId)
    override suspend fun upsertSubtask(subtask: SubtaskEntity) = subtaskDao.upsert(subtask)
    override suspend fun setSubtaskDone(id: Long, done: Boolean) =
        subtaskDao.setDone(id, done)
    override suspend fun deleteSubtask(subtask: SubtaskEntity) = subtaskDao.delete(subtask)
}
