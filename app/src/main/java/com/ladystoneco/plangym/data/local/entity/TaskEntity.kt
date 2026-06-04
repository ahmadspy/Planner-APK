package com.ladystoneco.plangym.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ladystoneco.plangym.domain.model.Priority
import com.ladystoneco.plangym.domain.model.Recurrence

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("projectId")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val projectId: Long? = null,
    val priority: Priority = Priority.MEDIUM,
    val recurrence: Recurrence = Recurrence.NONE,
    val dueAt: Long? = null,
    val reminderAt: Long? = null,
    val isDone: Boolean = false,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
