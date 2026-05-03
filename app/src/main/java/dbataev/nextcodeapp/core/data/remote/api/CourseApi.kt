package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.CourseDto
import retrofit2.http.GET

interface CourseApi {
    @GET("api/courses/all")
    suspend fun getAllCourses(): List<CourseDto>
}