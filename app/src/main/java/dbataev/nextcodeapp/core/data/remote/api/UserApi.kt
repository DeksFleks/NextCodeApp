package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.UserDto
import retrofit2.http.GET

interface UserApi {
    @GET("api/users/me")
    suspend fun getCurrentUser(): UserDto
}