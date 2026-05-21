package dbataev.nextcodeapp.feature.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dbataev.nextcodeapp.app.navigation.Screen
import dbataev.nextcodeapp.core.data.local.TokenStorage
import dbataev.nextcodeapp.core.designsystem.component.NcNullBottomBar
import dbataev.nextcodeapp.core.designsystem.component.NcNullTopBar
import dbataev.nextcodeapp.core.designsystem.component.NextCodeBackButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeTextField
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcErrorColor

@Composable
fun SettingScreen(
    navController: NavHostController,
    settingViewModel: SettingViewModel = viewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingTopBar(
            onBackClick = {
                navController.popBackStack()
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Пользователь:",
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasBook24
            )

            NextCodeTextField(
                value = settingViewModel.username,
                onValueChange = settingViewModel::onUsernameChange,
                placeholderText = "Новый логин",
                errorText = settingViewModel.usernameError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            NextCodeTextField(
                value = settingViewModel.nickname,
                onValueChange = settingViewModel::onNicknameChange,
                placeholderText = "Новый никнейм",
                errorText = settingViewModel.nicknameError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Пароль:",
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasBook24
            )

            NextCodeTextField(
                value = settingViewModel.password,
                onValueChange = settingViewModel::onPasswordChange,
                placeholderText = "Новый пароль",
                errorText = settingViewModel.passwordError,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            NextCodeTextField(
                value = settingViewModel.repeatPassword,
                onValueChange = settingViewModel::onRepeatPasswordChange,
                placeholderText = "Повторите новый пароль",
                errorText = settingViewModel.repeatPasswordError,
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            NextCodeButton(
                text = "Выйти из профиля",
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NcErrorColor
                ),
                onClick = {
                    TokenStorage(context).clearToken()

                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            ) { }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            NcNullBottomBar(
                modifier = Modifier.fillMaxWidth()
            )

            NextCodeButton(
                text = "Сохранить изменения",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = {
                    settingViewModel.updateUser()
                }
            ) { }
        }
    }
}

@Composable
private fun SettingTopBar(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        NcNullTopBar(
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            NextCodeBackButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Text(
                text = "Настройки",
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasBold36,
                modifier = Modifier.align(Alignment.CenterEnd)
                    .offset(y = 5.dp)
            )
        }
    }
}