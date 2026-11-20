package dbataev.nextcodeapp.feature.compiler

import androidx.compose.runtime.Composable

@Composable
fun CompilerScreen(){
    if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) {
        dbataev.nextcodeapp.core.designsystem.component.FullVersionScreen("Компилятор", "Запуск кода и результат выполнения появятся в полной версии.")
        return
    }

}