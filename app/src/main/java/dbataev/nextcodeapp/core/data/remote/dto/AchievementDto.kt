package dbataev.nextcodeapp.core.data.remote.dto

import dbataev.nextcodeapp.core.common.AchievementType

class AchievementDto (
    val title: String,
    val description: String,
    val type: AchievementType,
    val conditionValue: Int
)