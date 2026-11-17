package dbataev.nextcodeapp.core.data.remote.api
import dbataev.nextcodeapp.core.data.remote.dto.*
import retrofit2.http.*
interface TaskApi {
    @GET("api/tasks/bylesson/{lessonId}")
    suspend fun getTasksByLesson(@Path("lessonId") lessonId: Long): List<TaskDto>
    @POST("api/tasks/{taskId}/check")
    suspend fun check(@Path("taskId") taskId: Long, @Body answer: TaskAnswerDto): TaskResultDto
}
