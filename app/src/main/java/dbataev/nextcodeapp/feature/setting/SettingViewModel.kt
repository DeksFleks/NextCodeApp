package dbataev.nextcodeapp.feature.setting

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.data.remote.dto.UpdateUserDto
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set

    var nickname by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var repeatPassword by mutableStateOf("")
        private set

    var usernameError by mutableStateOf<String?>(null)
        private set

    var nicknameError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var repeatPasswordError by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    var generalError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onUsernameChange(value: String) {
        username = value
        usernameError = null
        successMessage = null
        generalError = null
    }

    fun onNicknameChange(value: String) {
        nickname = value
        nicknameError = null
        successMessage = null
        generalError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        passwordError = null
        successMessage = null
        generalError = null
    }

    fun onRepeatPasswordChange(value: String) {
        repeatPassword = value
        repeatPasswordError = null
        successMessage = null
        generalError = null
    }

    fun updateUser(
        onSuccess: () -> Unit = {}
    ) {
        usernameError = null
        nicknameError = null
        passwordError = null
        repeatPasswordError = null
        successMessage = null
        generalError = null

        val trimmedUsername = username.trim()
        val trimmedNickname = nickname.trim()

        val usernameForUpdate = trimmedUsername.takeIf { it.isNotBlank() }
        val nicknameForUpdate = trimmedNickname.takeIf { it.isNotBlank() }
        val passwordForUpdate = password.takeIf { it.isNotBlank() }

        if (usernameForUpdate == null && nicknameForUpdate == null && passwordForUpdate == null) {
            generalError = "Нет данных для обновления"
            return
        }

        if (usernameForUpdate != null && usernameForUpdate.length < 4) {
            usernameError = "Логин должен содержать не менее 4 символов"
        }

        if (nicknameForUpdate != null && nicknameForUpdate.length < 4) {
            nicknameError = "Никнейм должен содержать не менее 4 символов"
        }

        if (passwordForUpdate != null) {
            if (repeatPassword.isBlank()) {
                repeatPasswordError = "Повторите пароль"
            }

            if (password != repeatPassword && repeatPassword.isNotBlank()) {
                repeatPasswordError = "Пароли не совпадают"
            }

            if (!(password.any { it.isLetter() } && password.any { it.isDigit() })) {
                passwordError = "Пароль должен содержать буквы и цифры"
            }

            if (password.length < 8) {
                passwordError = "Пароль должен содержать не менее 8 символов"
            }
        }

        if (
            usernameError != null ||
            nicknameError != null ||
            passwordError != null ||
            repeatPasswordError != null
        ) {
            Log.d(
                "SETTING_DEBUG",
                "VALIDATION ERROR: username=$usernameError nickname=$nicknameError password=$passwordError repeat=$repeatPasswordError"
            )
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true

                RetrofitClient.userApi.updateUser(
                    UpdateUserDto(
                        username = usernameForUpdate,
                        nickname = nicknameForUpdate,
                        password = passwordForUpdate
                    )
                )

                successMessage = "Профиль обновлён"

                password = ""
                repeatPassword = ""

                onSuccess()

            } catch (e: Exception) {
                Log.e("SETTING_DEBUG", "UPDATE USER ERROR", e)
                generalError = "Не удалось обновить профиль"
            } finally {
                isLoading = false
            }
        }
    }
}