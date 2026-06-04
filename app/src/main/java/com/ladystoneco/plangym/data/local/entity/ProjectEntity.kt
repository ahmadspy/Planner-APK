package com.ladystoneco.plangym.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: Long,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
