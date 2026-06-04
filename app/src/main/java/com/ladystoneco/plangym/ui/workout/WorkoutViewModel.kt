package com.ladystoneco.plangym.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.RoutineEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSessionEntity
import com.ladystoneco.plangym.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    val routines: StateFlow<List<RoutineEntity>> =
        repository.observeRoutines()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val sessions: StateFlow<List<WorkoutSessionEntity>> =
        repository.observeSessions()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun addRoutine(name: String, note: String?) = viewModelScope.launch {
        repository.upsertRoutine(RoutineEntity(name = name.trim(), description = note))
    }

    fun deleteRoutine(routine: RoutineEntity) = viewModelScope.launch {
        repository.deleteRoutine(routine)
    }
}
