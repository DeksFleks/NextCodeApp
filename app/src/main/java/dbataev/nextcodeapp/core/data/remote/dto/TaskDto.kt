package dbataev.nextcodeapp.core.data.remote.dto
import dbataev.nextcodeapp.core.common.TaskType
import java.io.Serializable

data class TaskOptionDto(val id: Long, val text: String) : Serializable
data class TaskDto(
    val id: Long,
    val type: TaskType,
    val question: String,
    val options: List<TaskOptionDto>?,
    val multipleSelect: Boolean = false,
    val starterCode: String? = null,
    val programmingLanguage: String? = null,
    val sentenceBefore: String? = null,
    val sentenceAfter: String? = null
) : Serializable

data class TaskAnswerDto(val lessonId: Long, val optionIds: List<Long>? = null, val answer: String? = null)
data class TaskResultDto(val correct: Boolean, val explanation: String?, val validationMode: String)
