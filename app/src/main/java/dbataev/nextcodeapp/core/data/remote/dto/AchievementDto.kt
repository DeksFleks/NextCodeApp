package dbataev.nextcodeapp.core.data.remote.dto

import dbataev.nextcodeapp.core.common.AchievementType
import dbataev.nextcodeapp.core.common.LevelAchievementType

class AchievementDto (
    val title: String,
    val description: String,
    val type: AchievementType,
    val conditionValue: Int,
    val levelType: LevelAchievementType
)