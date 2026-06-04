package com.ladystoneco.plangym.data.local.converter

import androidx.room.TypeConverter
import com.ladystoneco.plangym.domain.model.ExerciseType
import com.ladystoneco.plangym.domain.model.Priority
import com.ladystoneco.plangym.domain.model.Recurrence

class Converters {

    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter
    fun fromRecurrence(value: Recurrence): String = value.name

    @TypeConverter
    fun toRecurrence(value: String): Recurrence = Recurrence.valueOf(value)

    @TypeConverter
    fun fromExerciseType(value: ExerciseType): String = value.name

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType = ExerciseType.valueOf(value)
}
