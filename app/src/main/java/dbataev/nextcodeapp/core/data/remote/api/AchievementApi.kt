package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.AchievementDto
import dbataev.nextcodeapp.core.data.remote.dto.QuoteDto
import retrofit2.http.GET

interface AchievementApi {
    @GET("api/achievement/all")
    suspend fun getAllAchievements(): List<AchievementDto>
}