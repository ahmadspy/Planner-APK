package com.ladystoneco.plangym.ui.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.ProjectEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlannerUiState(
    val projects: List<ProjectEntity> = emptyList(),
    val tasks: List<TaskEntity> = emptyList(),
    val isLoading: Boolean = true
) {
    val openTasks: List<TaskEntity> get() = tasks.filter { !it.isDone }
    val doneTasks: List<TaskEntity> get() = tasks.filter { it.isDone }
}

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val repository: PlannerRepository
) : ViewModel() {

    val state: StateFlow<PlannerUiState> =
        combine(
            repository.observeAllProjects(),
            repository.observeAllTasks()
        ) { projects, tasks ->
            PlannerUiState(
                projects = projects,
                tasks = tasks,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlannerUiState()
        )

    fun addTask(task: TaskEntity) = viewModelScope.launch {
        repository.upsertTask(task)
    }

    fun toggleTask(task: TaskEntity) = viewModelScope.launch {
        repository.setTaskDone(task.id, !task.isDone, if (!task.isDone) System.currentTimeMillis() else null)
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun addProject(name: String) = viewModelScope.launch {
        repository.upsertProject(ProjectEntity(name = name, color = 0L))
    }

    fun deleteProject(project: ProjectEntity) = viewModelScope.launch {
        repository.deleteProject(project)
    }

    fun renameProject(project: ProjectEntity, newName: String) = viewModelScope.launch {
        repository.upsertProject(project.copy(name = newName))
    }
}
