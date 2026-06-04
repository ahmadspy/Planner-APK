package com.ladystoneco.plangym.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.ExerciseEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity
import com.ladystoneco.plangym.data.local.relation.ExerciseWithSets
import com.ladystoneco.plangym.domain.repository.WorkoutRepository
import com.ladystoneco.plangym.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class RestTimerState(
    val isRunning: Boolean = false,
    val remainingSeconds: Int = 0,
    val totalSeconds: Int = 0
)

@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    private val repository: WorkoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routineId: Long = checkNotNull(savedStateHandle[Destination.RoutineDetail.ARG_ROUTINE_ID])

    val exercises: StateFlow<List<ExerciseWithSets>> =
        repository.observeRoutineDetail(routineId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _restTimerState = MutableStateFlow(RestTimerState())
    val restTimerState = _restTimerState.asStateFlow()
    
    private var timerJob: Job? = null

    fun addExercise(name: String, restSec: Int, order: Int) = viewModelScope.launch {
        repository.upsertExercise(
            ExerciseEntity(routineId = routineId, name = name.trim(), restSec = restSec, orderIndex = order)
        )
    }

    fun deleteExercise(exercise: ExerciseEntity) = viewModelScope.launch {
        repository.deleteExercise(exercise)
    }

    fun addSet(exerciseId: Long, reps: Int, weight: Float, setNumber: Int) = viewModelScope.launch {
        repository.upsertSet(
            WorkoutSetEntity(
                exerciseId = exerciseId,
                reps = reps,
                weight = weight,
                setNumber = setNumber,
                sessionDate = LocalDate.now().toEpochDay()
            )
        )
    }

    fun toggleSet(set: WorkoutSetEntity, restSec: Int = 60) = viewModelScope.launch {
        val newState = !set.isCompleted
        repository.setSetCompleted(set.id, newState)
        
        if (newState) {
            startRestTimer(restSec)
        } else {
            cancelRestTimer()
        }
    }

    fun startRestTimer(seconds: Int) {
        timerJob?.cancel()
        _restTimerState.value = RestTimerState(isRunning = true, remainingSeconds = seconds, totalSeconds = seconds)
        
        timerJob = viewModelScope.launch {
            while (_restTimerState.value.remainingSeconds > 0) {
                delay(1000)
                _restTimerState.value = _restTimerState.value.copy(
                    remainingSeconds = _restTimerState.value.remainingSeconds - 1
                )
            }
            _restTimerState.value = _restTimerState.value.copy(isRunning = false)
        }
    }

    fun cancelRestTimer() {
        timerJob?.cancel()
        _restTimerState.value = RestTimerState(isRunning = false)
    }

    fun deleteSet(set: WorkoutSetEntity) = viewModelScope.launch {
        repository.deleteSet(set)
    }
}
