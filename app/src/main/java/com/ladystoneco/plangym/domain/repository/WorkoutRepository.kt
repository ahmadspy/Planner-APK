package com.ladystoneco.plangym.domain.repository

import com.ladystoneco.plangym.data.local.entity.ExerciseEntity
import com.ladystoneco.plangym.data.local.entity.RoutineEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSessionEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity
import com.ladystoneco.plangym.data.local.relation.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {

    // Sessions
    fun observeSessions(): Flow<List<WorkoutSessionEntity>>
    suspend fun upsertSession(session: WorkoutSessionEntity): Long
    suspend fun deleteSession(session: WorkoutSessionEntity)

    // Routines
    fun observeRoutines(): Flow<List<RoutineEntity>>
    fun observeRoutine(id: Long): Flow<RoutineEntity?>
    fun observeRoutineDetail(routineId: Long): Flow<List<ExerciseWithSets>>
    suspend fun upsertRoutine(routine: RoutineEntity)
    suspend fun deleteRoutine(routine: RoutineEntity)

    // Exercises
    fun observeExercisesByRoutine(routineId: Long): Flow<List<ExerciseEntity>>
    fun observeExercise(id: Long): Flow<ExerciseEntity?>
    suspend fun upsertExercise(exercise: ExerciseEntity)
    suspend fun deleteExercise(exercise: ExerciseEntity)

    // Sets
    fun observeSetsByExercise(exerciseId: Long): Flow<List<WorkoutSetEntity>>
    suspend fun upsertSet(set: WorkoutSetEntity)
    suspend fun setSetCompleted(id: Long, completed: Boolean)
    suspend fun deleteSet(set: WorkoutSetEntity)
}
