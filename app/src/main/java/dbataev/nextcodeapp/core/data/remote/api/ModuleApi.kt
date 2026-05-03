package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.ModuleDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ModuleApi {

    @GET("api/modules/incourse/{courseId}")
    suspend fun getModulesByCourseId(
        @Path("courseId") courseId: Long
    ): List<ModuleDto>
}