package com.ladystoneco.plangym.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.ladystoneco.plangym.data.local.entity.ExerciseEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity

data class ExerciseWithSets(
    @Embedded val exercise: ExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val sets: List<WorkoutSetEntity>
)
