package com.ladystoneco.plangym.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.domain.repository.HabitRepository
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import com.ladystoneco.plangym.util.AppTheme
import com.ladystoneco.plangym.util.LocaleDataStore
import com.ladystoneco.plangym.util.LocalizationManager
import com.ladystoneco.plangym.util.ThemeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val quoteIndex: Int = 0,
    val today: LocalDate = LocalDate.now(),
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val totalHabits: Int = 0,
    val completedHabits: Int = 0,
    val locale: String = "fa",
    val theme: AppTheme = AppTheme.GLASS_VIOLET,
    val greetingResId: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plannerRepository: PlannerRepository,
    private val habitRepository: HabitRepository,
    private val localeDataStore: LocaleDataStore,
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    private val _quoteIndex = MutableStateFlow(0)
    
    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<HomeUiState> = combine(
        _quoteIndex,
        plannerRepository.observeAllTasks(),
        habitRepository.observeActiveHabits(),
        habitRepository.observeAllLogs(),
        localeDataStore.locale,
        themeDataStore.theme
    ) { args: Array<Any> ->
        val quoteIdx = args[0] as Int
        val tasks = args[1] as List<TaskEntity>
        val habits = args[2] as List<HabitEntity>
        val logs = args[3] as List<HabitLogEntity>
        val locale = args[4] as String
        val theme = args[5] as AppTheme

        val todayEpoch = LocalDate.now().toEpochDay()
        val completedHabitsCount = logs.count { it.dateEpochDay == todayEpoch && it.isCompleted }
        
        val hour = java.time.LocalTime.now().hour
        val greetingResId = when {
            hour < 12 -> com.ladystoneco.plangym.R.string.good_morning
            hour < 18 -> com.ladystoneco.plangym.R.string.good_afternoon
            else -> com.ladystoneco.plangym.R.string.good_evening
        }
        
        HomeUiState(
            quoteIndex = quoteIdx,
            today = LocalDate.now(),
            totalTasks = tasks.size,
            completedTasks = tasks.count { it.isDone },
            totalHabits = habits.size,
            completedHabits = completedHabitsCount,
            locale = locale,
            theme = theme,
            greetingResId = greetingResId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun refreshQuote() {
        _quoteIndex.value = (0..4).random()
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val current = uiState.value.locale
            val next = if (current == "fa") "en" else "fa"
            localeDataStore.setLocale(next)
            LocalizationManager.setLocale(next)
        }
    }

    fun nextTheme() {
        viewModelScope.launch {
            val current = uiState.value.theme
            val next = when (current) {
                AppTheme.CALM_MINIMAL -> AppTheme.GLASS_VIOLET
                AppTheme.GLASS_VIOLET -> AppTheme.FITNESS_DARK
                AppTheme.FITNESS_DARK -> AppTheme.CALM_MINIMAL
            }
            themeDataStore.setTheme(next)
        }
    }
}
