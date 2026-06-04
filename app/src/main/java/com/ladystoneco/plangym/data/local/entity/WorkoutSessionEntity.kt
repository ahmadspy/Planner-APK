package com.ladystoneco.plangym.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ladystoneco.plangym.domain.model.WorkoutSessionStatus

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val routineId: Long? = null,
    val title: String,
    val startedAt: Long,
    val endedAt: Long? = null,
    val durationSeconds: Int = 0,
    val status: WorkoutSessionStatus = WorkoutSessionStatus.ACTIVE,
    val notes: String? = null
)
