package com.ladystoneco.plangym.ui.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import com.ladystoneco.plangym.domain.model.Recurrence
import com.ladystoneco.plangym.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HabitUi(
    val habit: HabitEntity,
    val doneToday: Boolean,
    val streak: Int
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    val habits: StateFlow<List<HabitUi>> =
        combine(
            repository.observeActiveHabits(),
            repository.observeAllLogs()
        ) { habits: List<HabitEntity>, logs: List<HabitLogEntity> ->
            val todayEpoch = LocalDate.now().toEpochDay()
            
            val doneByHabit: Map<Long, Set<Long>> =
                logs.filter { it.isCompleted }
                    .groupBy { it.habitId }
                    .mapValues { entry -> entry.value.map { it.dateEpochDay }.toSet() }

            habits.map { habit ->
                val dates = doneByHabit[habit.id].orEmpty()
                HabitUi(
                    habit = habit,
                    doneToday = todayEpoch in dates,
                    streak = currentStreak(todayEpoch, dates)
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun currentStreak(todayEpoch: Long, dates: Set<Long>): Int {
        var count = 0
        var cursor = todayEpoch
        while (cursor in dates) {
            count++
            cursor--
        }
        return count
    }

    fun addHabit(name: String, recurrence: Recurrence) = viewModelScope.launch {
        repository.upsertHabit(HabitEntity(name = name.trim(), recurrence = recurrence, color = 0L))
    }

    fun toggleToday(ui: HabitUi) = viewModelScope.launch {
        val todayEpoch = LocalDate.now().toEpochDay()
        val existing = repository.getLogForDate(ui.habit.id, todayEpoch)
        if (existing == null) {
            repository.upsertLog(
                HabitLogEntity(
                    habitId = ui.habit.id,
                    dateEpochDay = todayEpoch,
                    isCompleted = true
                )
            )
        } else {
            repository.upsertLog(existing.copy(isCompleted = !existing.isCompleted))
        }
    }

    fun deleteHabit(habit: HabitEntity) = viewModelScope.launch {
        repository.deleteHabit(habit)
    }
}
