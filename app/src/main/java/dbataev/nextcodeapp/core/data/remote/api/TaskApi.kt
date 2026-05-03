package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.QuoteDto
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskApi {
    @GET("api/tasks/bylesson/{lessonId}")
        suspend fun getTasksByLesson(@Path("lessonId") lessonId: Long): List<TaskDto>
}