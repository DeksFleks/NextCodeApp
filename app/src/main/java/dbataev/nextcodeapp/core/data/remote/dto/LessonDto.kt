package dbataev.nextcodeapp.core.data.remote.dto

import dbataev.nextcodeapp.core.common.LessonState

data class LessonDto(
    val id: Long,
    val title: String,
    val xpReward: Int,
    val theoryText: String?,
    val state: LessonState,
)