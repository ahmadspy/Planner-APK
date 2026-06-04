package com.ladystoneco.plangym.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity

data class TaskWithSubtasks(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val subtasks: List<SubtaskEntity>
)
