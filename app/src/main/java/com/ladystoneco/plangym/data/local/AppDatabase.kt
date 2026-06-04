package com.ladystoneco.plangym.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ladystoneco.plangym.data.local.converter.Converters
import com.ladystoneco.plangym.data.local.dao.HabitDao
import com.ladystoneco.plangym.data.local.dao.ProjectDao
import com.ladystoneco.plangym.data.local.dao.SubtaskDao
import com.ladystoneco.plangym.data.local.dao.TaskDao
import com.ladystoneco.plangym.data.local.dao.WorkoutDao
import com.ladystoneco.plangym.data.local.entity.ExerciseEntity
import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import com.ladystoneco.plangym.data.local.entity.ProjectEntity
import com.ladystoneco.plangym.data.local.entity.RoutineEntity
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSessionEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity

@Database(
    entities = [
        ProjectEntity::class,
        TaskEntity::class,
        SubtaskEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        RoutineEntity::class,
        ExerciseEntity::class,
        WorkoutSetEntity::class,
        WorkoutSessionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao
    abstract fun habitDao(): HabitDao
    abstract fun workoutDao(): WorkoutDao
}
