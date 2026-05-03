package dbataev.nextcodeapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dbataev.nextcodeapp.app.navigation.MainScreen
import dbataev.nextcodeapp.core.data.local.TokenStorage
import dbataev.nextcodeapp.core.data.remote.RetrofitClient
import dbataev.nextcodeapp.feature.auth.log.LoginScreen
import dbataev.nextcodeapp.feature.auth.reg.RegisterScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenStorage = TokenStorage(applicationContext)
        RetrofitClient.init(tokenStorage)


        setContent {
            MainScreen()
        }
    }
}
