package com.ladystoneco.plangym.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ladystoneco.plangym.domain.repository.HabitRepository
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import com.ladystoneco.plangym.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val plannerRepository: PlannerRepository,
    private val habitRepository: HabitRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val notificationHelper = NotificationHelper(applicationContext)
        val todayEpoch = LocalDate.now().toEpochDay()

        // Check for tasks with reminders
        val tasksWithReminders = plannerRepository.getTasksWithReminders()
        tasksWithReminders.forEach { task ->
            if (!task.isDone) {
                notificationHelper.showNotification(
                    title = "یادآور تسک",
                    message = task.title
                )
            }
        }

        // Check for habits with reminders
        val habitsWithReminders = habitRepository.getHabitsWithReminders()
        habitsWithReminders.forEach { habit ->
            val log = habitRepository.getLogForDate(habit.id, todayEpoch)
            if (log == null || !log.isCompleted) {
                notificationHelper.showNotification(
                    title = "عادت روزانه",
                    message = "فراموش نکردی که ${habit.name} رو انجام بدی؟"
                )
            }
        }

        return Result.success()
    }
}
