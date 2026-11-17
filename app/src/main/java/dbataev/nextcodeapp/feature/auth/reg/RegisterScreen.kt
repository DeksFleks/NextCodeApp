package dbataev.nextcodeapp.feature.auth.reg

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.designsystem.component.*
import dbataev.nextcodeapp.feature.auth.AuthFormLayout

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit) {
    val viewModel: RegisterViewModel = viewModel()
    val context = LocalContext.current
    AuthFormLayout(title = "Регистрация", footer = "Уже есть аккаунт? Войти", onFooterClick = onLoginClick) {
        NextCodeTextField(value = viewModel.username, onValueChange = viewModel::onUsernameChange,
            placeholderText = "Введите логин", errorText = viewModel.usernameError, modifier = Modifier.fillMaxWidth())
        NextCodeTextField(value = viewModel.password, onValueChange = viewModel::onPasswordChange,
            placeholderText = "Введите пароль", errorText = viewModel.passwordError, isPassword = true,
            modifier = Modifier.fillMaxWidth())
        NextCodeTextField(value = viewModel.repeatPassword, onValueChange = viewModel::onRepeatPasswordChange,
            placeholderText = "Введите пароль ещё раз", errorText = viewModel.repeatPasswordError, isPassword = true,
            modifier = Modifier.fillMaxWidth())
        NextCodeTextField(value = viewModel.nickname, onValueChange = viewModel::onNicknameChange,
            placeholderText = "Введите никнейм", errorText = viewModel.nicknameError, modifier = Modifier.fillMaxWidth())
        NextCodeButton(text = "Зарегистрироваться", modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            onClick = { viewModel.register(context, onRegisterSuccess) }) { }
    }
}
