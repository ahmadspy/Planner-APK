package com.ladystoneco.plangym.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ladystoneco.plangym.domain.model.EquipmentType
import com.ladystoneco.plangym.domain.model.ExerciseCategory
import com.ladystoneco.plangym.domain.model.ExerciseType
import com.ladystoneco.plangym.domain.model.MuscleGroup

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val routineId: Long,
    val name: String,
    val type: ExerciseType = ExerciseType.REPS,
    val category: ExerciseCategory = ExerciseCategory.STRENGTH,
    val muscleGroup: MuscleGroup = MuscleGroup.OTHER,
    val equipment: EquipmentType = EquipmentType.NONE,
    val targetSets: Int = 3,
    val targetReps: Int? = null,
    val targetWeight: Float? = null,
    val targetDurationSec: Int? = null,
    val restSec: Int = 60,
    val orderIndex: Int = 0,
    val note: String? = null
)
