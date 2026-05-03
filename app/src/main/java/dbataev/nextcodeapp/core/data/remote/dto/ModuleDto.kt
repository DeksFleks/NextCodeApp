package dbataev.nextcodeapp.core.data.remote.dto

data class ModuleDto(
    val id: Long,
    val courseId: Long,
    val title: String,
    val orderIndex: Int,
    val lessons: List<LessonDto>
)