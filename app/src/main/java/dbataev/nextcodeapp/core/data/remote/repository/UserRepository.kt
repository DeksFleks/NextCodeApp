package dbataev.nextcodeapp.core.data.remote.repository

import dbataev.nextcodeapp.core.data.remote.api.UserApi
import dbataev.nextcodeapp.core.data.remote.dto.UserDto

class UserRepository(private val api: UserApi) {
    suspend fun getCurrentUser(): UserDto {
        return api.getCurrentUser()
    }
}