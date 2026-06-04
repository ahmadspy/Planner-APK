package com.ladystoneco.plangym.ui.planner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.ProjectEntity
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.data.local.relation.TaskWithSubtasks
import com.ladystoneco.plangym.domain.model.Priority
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import com.ladystoneco.plangym.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectDetailUiState(
    val project: ProjectEntity? = null,
    val tasks: List<TaskWithSubtasks> = emptyList(),
    val progress: Float = 0f,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val repository: PlannerRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val projectId: Long = checkNotNull(savedStateHandle[Destination.ProjectDetail.ARG_PROJECT_ID])

    val uiState: StateFlow<ProjectDetailUiState> =
        combine(
            repository.observeProject(projectId),
            repository.observeTasksWithSubtasks(projectId)
        ) { project: ProjectEntity?, tasks: List<TaskWithSubtasks> ->
            val doneCount = tasks.count { it.task.isDone }
            ProjectDetailUiState(
                project = project,
                tasks = tasks,
                progress = if (tasks.isEmpty()) 0f else doneCount.toFloat() / tasks.size,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProjectDetailUiState()
        )

    fun addTask(title: String, priority: Priority) = viewModelScope.launch {
        repository.upsertTask(
            TaskEntity(projectId = projectId, title = title.trim(), priority = priority)
        )
    }

    fun toggleTask(task: TaskEntity) = viewModelScope.launch {
        repository.setTaskDone(task.id, !task.isDone, if (!task.isDone) System.currentTimeMillis() else null)
    }

    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    // --- Subtasks ---
    fun addSubtask(taskId: Long, title: String) = viewModelScope.launch {
        repository.upsertSubtask(SubtaskEntity(taskId = taskId, title = title.trim()))
    }

    fun toggleSubtask(subtask: SubtaskEntity) = viewModelScope.launch {
        repository.setSubtaskDone(subtask.id, !subtask.isDone)
    }

    fun deleteSubtask(subtask: SubtaskEntity) = viewModelScope.launch {
        repository.deleteSubtask(subtask)
    }
}
