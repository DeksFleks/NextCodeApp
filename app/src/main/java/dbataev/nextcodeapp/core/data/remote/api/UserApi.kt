package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.UpdateUserDto
import dbataev.nextcodeapp.core.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("api/users/me")
    suspend fun getCurrentUser(): UserDto

    @PUT("api/users/course/{courseId}")
    suspend fun setCurrentCourse(
        @Path("courseId") courseId: Long
    )

    @PUT("api/users/update")
    suspend fun updateUser(
        @Body userDto: UpdateUserDto
    )
}