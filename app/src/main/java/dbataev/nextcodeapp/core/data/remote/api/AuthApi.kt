package dbataev.nextcodeapp.core.data.remote.api

import dbataev.nextcodeapp.core.data.remote.dto.CreateUserDto
import dbataev.nextcodeapp.core.data.remote.dto.JwtDto
import dbataev.nextcodeapp.core.data.remote.dto.LoginUserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body dto: LoginUserDto): JwtDto

    @POST("api/auth/register")
    suspend fun register(@Body dto: CreateUserDto): JwtDto
}