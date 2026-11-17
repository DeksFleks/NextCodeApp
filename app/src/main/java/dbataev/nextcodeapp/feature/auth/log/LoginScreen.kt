package dbataev.nextcodeapp.feature.auth.log

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.designsystem.component.*
import dbataev.nextcodeapp.feature.auth.AuthFormLayout

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit, sessionExpired: Boolean = false) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    AuthFormLayout(title = "Вход", footer = "Еще нет аккаунта? Зарегистрироваться", onFooterClick = onRegisterClick,
        notice = if (sessionExpired) "Сессия истекла. Войдите снова." else null) {
        NextCodeTextField(value = viewModel.username, onValueChange = viewModel::onUsernameChange,
            placeholderText = "Введите логин", errorText = viewModel.usernameError, modifier = Modifier.fillMaxWidth())
        NextCodeTextField(value = viewModel.password, onValueChange = viewModel::onPasswordChange,
            placeholderText = "Введите пароль", errorText = viewModel.passwordError, isPassword = true,
            modifier = Modifier.fillMaxWidth())
        NextCodeButton(text = "Войти", modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            onClick = { viewModel.login(context, onLoginSuccess) }) { }
    }
}
