package com.ladystoneco.plangym.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId")]
)
data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exerciseId: Long,
    val sessionId: Long? = null, // Link to WorkoutSession
    val sessionDate: Long,
    val setNumber: Int,
    val reps: Int? = null,
    val weight: Float? = null,
    val durationSec: Int? = null,
    val isCompleted: Boolean = false
)
