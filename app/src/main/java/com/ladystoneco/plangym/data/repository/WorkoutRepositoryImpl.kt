package com.ladystoneco.plangym.data.repository

import com.ladystoneco.plangym.data.local.dao.WorkoutDao
import com.ladystoneco.plangym.data.local.entity.ExerciseEntity
import com.ladystoneco.plangym.data.local.entity.RoutineEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSessionEntity
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity
import com.ladystoneco.plangym.data.local.relation.ExerciseWithSets
import com.ladystoneco.plangym.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    // Sessions
    override fun observeSessions(): Flow<List<WorkoutSessionEntity>> = workoutDao.observeSessions()
    override suspend fun upsertSession(session: WorkoutSessionEntity): Long = workoutDao.upsertSession(session)
    override suspend fun deleteSession(session: WorkoutSessionEntity) = workoutDao.deleteSession(session)

    // Routines
    override fun observeRoutines(): Flow<List<RoutineEntity>> = workoutDao.observeRoutines()
    override fun observeRoutine(id: Long): Flow<RoutineEntity?> = workoutDao.observeRoutine(id)
    override fun observeRoutineDetail(routineId: Long): Flow<List<ExerciseWithSets>> =
        workoutDao.observeRoutineDetail(routineId)
    override suspend fun upsertRoutine(routine: RoutineEntity) {
        workoutDao.upsertRoutine(routine)
    }
    override suspend fun deleteRoutine(routine: RoutineEntity) =
        workoutDao.deleteRoutine(routine)

    // Exercises
    override fun observeExercisesByRoutine(routineId: Long): Flow<List<ExerciseEntity>> =
        workoutDao.observeExercises(routineId)
    override fun observeExercise(id: Long): Flow<ExerciseEntity?> = workoutDao.observeExercise(id)
    override suspend fun upsertExercise(exercise: ExerciseEntity) {
        workoutDao.upsertExercise(exercise)
    }
    override suspend fun deleteExercise(exercise: ExerciseEntity) =
        workoutDao.deleteExercise(exercise)

    // Sets
    override fun observeSetsByExercise(exerciseId: Long): Flow<List<WorkoutSetEntity>> =
        workoutDao.observeSetHistory(exerciseId)
    override suspend fun upsertSet(set: WorkoutSetEntity) {
        workoutDao.upsertSet(set)
    }
    override suspend fun setSetCompleted(id: Long, completed: Boolean) =
        workoutDao.setSetCompleted(id, completed)
    override suspend fun deleteSet(set: WorkoutSetEntity) = workoutDao.deleteSet(set)
}
