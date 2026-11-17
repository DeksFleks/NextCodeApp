package dbataev.nextcodeapp.core.data.remote.dto

data class UserDto(
    val languageId: Long = 1,
    val id: Long,
    val username: String,
    val nickname: String,
    val xp: Int,
    val level: Int,
    val streak: Int,
    val xpForNextLevel: Int,
    val currentCourseId: Long?,
    val totalXp: Long,
    val bestStreak: Long,
    val completedLessons: Long,
)