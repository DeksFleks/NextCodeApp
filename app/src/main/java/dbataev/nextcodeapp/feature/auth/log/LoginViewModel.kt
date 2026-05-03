package dbataev.nextcodeapp.feature.auth.log

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.local.TokenStorage
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.LoginUserDto
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var usernameError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    fun onUsernameChange(value: String) {
        username = value
        usernameError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        passwordError = null
    }

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login(
        context: Context,
        onSuccess: () -> Unit
    ) {
        usernameError = null
        passwordError = null


        if (username.isBlank()) {
            usernameError = "Это поле не может быть пустым"
        }

        if (password.isBlank()) {
            passwordError = "Это поле не может быть пустым"
        }

        if (usernameError != null || passwordError != null) {
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val jwtDto = RetrofitClient.authApi.login(
                    LoginUserDto(
                        username = username,
                        password = password,
                    )
                )

                TokenStorage(context).saveToken(jwtDto.jwt)

                onSuccess()

            } catch (e: HttpException) {
                usernameError = when (e.code()) {
                    403 -> "Неверный логин или пароль"
                    401 -> "Неверный логин или пароль"
                    400 -> "Некорректные данные"
                    500 -> "Ошибка сервера"
                    else -> "Ошибка: ${e.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Нет подключения к серверу"
            } finally {
                isLoading = false
            }
        }
    }

}