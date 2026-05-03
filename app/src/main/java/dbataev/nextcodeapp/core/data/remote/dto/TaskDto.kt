package dbataev.nextcodeapp.core.data.remote.dto

import dbataev.nextcodeapp.core.common.TaskType
import java.io.Serializable

data class TaskDto(
    val id: Long,
    val type: TaskType,
    val question: String,
    val options: List<String>?,
    val correctAnswer: String,
    val explanation: String?
)  : Serializable