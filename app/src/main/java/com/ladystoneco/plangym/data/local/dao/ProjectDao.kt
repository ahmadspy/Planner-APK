package com.ladystoneco.plangym.data.local.dao

import androidx.room.*
import com.ladystoneco.plangym.data.local.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun observeActiveProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun observeAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun observeProject(id: Long): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProject(id: Long): ProjectEntity?

    @Upsert
    suspend fun upsert(project: ProjectEntity)

    @Update
    suspend fun update(project: ProjectEntity)

    @Query("UPDATE projects SET isArchived = :archived WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean)

    @Delete
    suspend fun delete(project: ProjectEntity)
}
