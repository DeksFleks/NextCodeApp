package dbataev.nextcodeapp.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dbataev.nextcodeapp.app.navigation.Screen
import dbataev.nextcodeapp.core.data.local.TokenStorage

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val token = TokenStorage(context).getToken()

        if (token != null) {
            navController.navigate(Screen.Home.createRoute(1L)) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Screen.Register.route) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
    }
}