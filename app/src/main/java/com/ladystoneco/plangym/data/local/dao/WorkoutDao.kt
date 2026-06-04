package com.ladystoneco.plangym.data.local.dao

import androidx.room.*
import com.ladystoneco.plangym.data.local.entity.ExerciseEntity
import com.ladystoneco.plangym.data.local.entity.RoutineEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSessionEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity
import com.ladystoneco.plangym.data.local.relation.ExerciseWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // ---- Sessions ----

    @Query("SELECT * FROM workout_sessions ORDER BY startedAt DESC")
    fun observeSessions(): Flow<List<WorkoutSessionEntity>>

    @Upsert
    suspend fun upsertSession(session: WorkoutSessionEntity): Long

    @Delete
    suspend fun deleteSession(session: WorkoutSessionEntity)

    // ---- Routines ----

    @Query("SELECT * FROM routines ORDER BY createdAt DESC")
    fun observeRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :id")
    fun observeRoutine(id: Long): Flow<RoutineEntity?>

    @Upsert
    suspend fun upsertRoutine(routine: RoutineEntity): Long

    @Update
    suspend fun updateRoutine(routine: RoutineEntity)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    // ---- Exercises ----

    @Query("SELECT * FROM exercises WHERE routineId = :routineId ORDER BY orderIndex ASC")
    fun observeExercises(routineId: Long): Flow<List<ExerciseEntity>>

    @Transaction
    @Query("SELECT * FROM exercises WHERE routineId = :routineId ORDER BY orderIndex ASC")
    fun observeRoutineDetail(routineId: Long): Flow<List<ExerciseWithSets>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    fun observeExercise(id: Long): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExercise(id: Long): ExerciseEntity?

    @Upsert
    suspend fun upsertExercise(exercise: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    // ---- Sets ----

    @Query(
        "SELECT * FROM workout_sets WHERE exerciseId = :exerciseId " +
            "AND sessionDate = :sessionDate ORDER BY setNumber ASC"
    )
    fun observeSets(exerciseId: Long, sessionDate: Long): Flow<List<WorkoutSetEntity>>

    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId ORDER BY sessionDate DESC")
    fun observeSetHistory(exerciseId: Long): Flow<List<WorkoutSetEntity>>

    @Upsert
    suspend fun upsertSet(set: WorkoutSetEntity): Long

    @Query("UPDATE workout_sets SET isCompleted = :completed WHERE id = :id")
    suspend fun setSetCompleted(id: Long, completed: Boolean)

    @Update
    suspend fun updateSet(set: WorkoutSetEntity)

    @Delete
    suspend fun deleteSet(set: WorkoutSetEntity)
}
