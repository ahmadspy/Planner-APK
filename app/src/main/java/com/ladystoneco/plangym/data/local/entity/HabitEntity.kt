package com.ladystoneco.plangym.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ladystoneco.plangym.domain.model.Recurrence

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val color: Long,
    val iconKey: String? = null,
    val recurrence: Recurrence = Recurrence.DAILY,
    val targetPerPeriod: Int = 1,
    val reminderAt: Long? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
