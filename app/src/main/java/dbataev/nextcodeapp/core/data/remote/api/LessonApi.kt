package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.AchievementDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface LessonApi {

    @POST("api/lesson/{lessonId}/completed")
    suspend fun lessonCompleted(
        @Path("lessonId") lessonId: Long
    ): Response<List<AchievementDto>>
}