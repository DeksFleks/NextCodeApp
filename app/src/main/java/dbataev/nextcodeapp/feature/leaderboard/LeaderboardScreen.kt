package dbataev.nextcodeapp.feature.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor

@Composable
fun LeaderboardScreen(){
    if (dbataev.nextcodeapp.BuildConfig.OFFLINE_DEMO) {
        dbataev.nextcodeapp.core.designsystem.component.FullVersionScreen("Рейтинг игроков", "Соревнования с другими игроками и общий рейтинг появятся в полной версии.")
        return
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NcBackgroundColor)
    ){
        NcBackgroundText()

        Text(
            text = "Лидерборд",
            color = NcAccentColor,
            style = DefaultAppTextStyles.hero
        )
    }
}