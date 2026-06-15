package dbataev.nextcodeapp.feature.auth.reg

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.local.TokenStorage
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.CreateUserDto
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var repeatPassword by mutableStateOf("")
        private set

    var nickname by mutableStateOf("")
        private set

    var usernameError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var repeatPasswordError by mutableStateOf<String?>(null)
        private set

    var nicknameError by mutableStateOf<(String?)>(null)

    fun onUsernameChange(value: String) {
        username = value
        usernameError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        passwordError = null
    }

    fun onRepeatPasswordChange(value: String) {
        repeatPassword = value
        repeatPasswordError = null
    }

    fun onNicknameChange(value: String) {
        nickname = value
        nicknameError = null
    }

    fun register(
        context: Context,
        onSuccess: () -> Unit
    ) {
        usernameError = null
        passwordError = null
        repeatPasswordError = null
        nicknameError = null

        if (username.length < 4) {
            usernameError = "Логин должен содержать не менее 4 символов"
        }

        if (username.isBlank()) {
            usernameError = "Это поле не может быть пустым"
        }

        if (repeatPassword.isBlank()) {
            repeatPasswordError = "Это поле не может быть пустым"
        }

        if (password.isNotBlank() && repeatPassword.isNotBlank() && password != repeatPassword) {
            repeatPasswordError = "Пароли не совпадают"
        }

        if (password.any { it.isLetter() } && password.any { it.isDigit() }) {

        } else {
            passwordError = "Пароль должен содержать буквы и цифры"
        }


        if (password.length < 8) {
            passwordError = "Пароль должен содержать не менее 8 символов"
        }

        if (password.isBlank()) {
            passwordError = "Это поле не может быть пустым"
        }

        if (!nickname.isBlank()) {
            if (nickname.length < 4) {
                nicknameError = "Никнейм должен содержать не менее 4 символов"
            }
        }

        if (username.length > 20) {
            usernameError = "Логин должен содержать не более 20 символов"
        }

        if (password.length > 64) {
            passwordError = "Пароль должен содержать не более 64 символов"
        }

        if (nickname.length > 20) {
            nicknameError = "Никнейм должен содержать не более 20 символов"
        }


        if (usernameError != null || passwordError != null || repeatPasswordError != null || nicknameError != null) {
            android.util.Log.d(
                "REGISTER_DEBUG",
                "VALIDATION ERROR: username=$usernameError password=$passwordError repeat=$repeatPasswordError nickname=$nicknameError"
            )
            return
        }
        viewModelScope.launch {
            try {
                val jwtDto = RetrofitClient.authApi.register(
                    CreateUserDto(
                        username = username,
                        password = password,
                        nickname = nickname
                    )
                )

                TokenStorage(context).saveToken(jwtDto.jwt)

                onSuccess()

            } catch (e: Exception) {
                android.util.Log.e("REGISTER_DEBUG", "REGISTER ERROR", e)
            }
        }
    }
}
