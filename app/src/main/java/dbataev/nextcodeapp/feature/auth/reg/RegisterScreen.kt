package dbataev.nextcodeapp.feature.auth.reg

import android.R
import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeTextField
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcLessonBlockedColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var viewModel: RegisterViewModel = viewModel()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NcBackgroundColor)
    ) {
        NcBackgroundText()

        Box(modifier = Modifier.fillMaxHeight()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .border(1.dp, NcSecondAccentColor, RoundedCornerShape(36.dp))
                    .background(NcMainColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 24.dp)
                    ) {
                        Text(
                            text = "Регистрация",
                            color = NcAccentColor,
                            style = DefaultAppTextStyles.bebasBold36
                        )
                    }
                    NextCodeTextField(
                        value = viewModel.username,
                        onValueChange = viewModel::onUsernameChange,
                        placeholderText = "Введите логин",
                        errorText = viewModel.usernameError,
                        modifier = Modifier.fillMaxWidth(0.92f)
                    )

                    NextCodeTextField(
                        value = viewModel.password,
                        onValueChange = viewModel::onPasswordChange,
                        isPassword = true,
                        placeholderText = "Введите пароль",
                        errorText = viewModel.passwordError,
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                    )
                    NextCodeTextField(
                        value = viewModel.repeatPassword,
                        onValueChange = viewModel::onRepeatPasswordChange,
                        isPassword = true,
                        placeholderText = "Введите пароль ещё раз",
                        errorText = viewModel.repeatPasswordError,
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                    )
                    NextCodeTextField(
                        value = viewModel.nickname,
                        onValueChange = viewModel::onNicknameChange,
                        placeholderText = "Введите никнейм",
                        errorText = viewModel.nicknameError,
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                    )
                    NextCodeButton(
                        text = "Зарегистрироваться",
                        modifier = Modifier
                            .fillMaxWidth(0.80f)
                            .padding(top = 24.dp),
                        onClick = {
                            viewModel.register(
                                context = context,
                                onSuccess = onRegisterSuccess
                            )
                        }
                    ) {

                    }
                }
            }
        }

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NcBackgroundColor)
        ) {
            Text(
                text = "Уже есть аккаунт? Войти",
                style = DefaultAppTextStyles.bebasBook14,
                color = NcLessonBlockedColor
            )
        }
    }
}
