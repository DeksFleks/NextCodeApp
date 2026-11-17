package dbataev.nextcodeapp.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dbataev.nextcodeapp.app.navigation.Screen
import dbataev.nextcodeapp.app.navigation.learningRoute
import dbataev.nextcodeapp.core.common.viewModel.UserViewModel
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import kotlinx.coroutines.CancellationException

@Composable
fun SplashScreen(navController: NavController, userViewModel: UserViewModel) {
    var attempt by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(attempt) {
        error = null
        if (RetrofitClient.session.ended.value != null) return@LaunchedEffect
        if (RetrofitClient.session.token() == null) {
            navController.navigate(Screen.Register.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
            return@LaunchedEffect
        }
        try {
            val user = userViewModel.refreshUser()
            navController.navigate(learningRoute(user.currentCourseId)) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } catch (e: CancellationException) { throw e }
        catch (e: Exception) {
            // A network outage keeps the token. Auth failures are handled globally.
            if (RetrofitClient.session.token() != null) error = "Не удалось проверить сессию. Проверьте соединение."
        }
    }
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(error ?: "Проверяем сессию…", color = NcAccentColor)
        if (error != null) NextCodeButton(text = "Повторить", onClick = { attempt++ }) { }
    }
}
