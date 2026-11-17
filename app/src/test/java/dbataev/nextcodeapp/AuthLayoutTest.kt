package dbataev.nextcodeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.feature.auth.log.LoginScreen
import dbataev.nextcodeapp.feature.auth.reg.RegisterScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w320dp-h480dp-mdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@LooperMode(LooperMode.Mode.PAUSED)
class AuthLayoutTest {
    @get:Rule val compose = createComposeRule()

    @Test fun registrationButtonAndFooterAreReachableInReducedViewport() {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f, 1.8f)) {
                Box(Modifier.requiredSize(320.dp, 280.dp)) { RegisterScreen({}, {}) }
            }
        }
        compose.onNodeWithText("Зарегистрироваться").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Уже есть аккаунт? Войти").performScrollTo().assertIsDisplayed()
    }
    @Test fun loginButtonRemainsReachableWithLargeFont() {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f, 2f)) {
                Box(Modifier.requiredSize(320.dp, 300.dp)) { LoginScreen({}, {}) }
            }
        }
        compose.onNodeWithText("Войти").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Еще нет аккаунта? Зарегистрироваться").performScrollTo().assertIsDisplayed()
    }
    @Test fun sessionExpiryNoticeIsReadableAndDoesNotHideLogin() {
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(1f, 1.5f)) {
                Box(Modifier.requiredSize(320.dp, 280.dp)) { LoginScreen({}, {}, sessionExpired = true) }
            }
        }
        compose.onNodeWithText("Сессия истекла. Войдите снова.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Войти").performScrollTo().assertIsDisplayed()
    }
}
