package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcCyanColor

@Composable
fun NextCodeModulesTitles(
    title: String,
    orderIndex: Int,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        ModuleLine(modifier = Modifier.weight(1f))

        Row(
            Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Модуль $orderIndex: ",
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasBook20
            )

            Text(
                text = "$title",
                color = NcCyanColor,
                style = DefaultAppTextStyles.bebasBook20
            )
        }
        ModuleLine(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ModuleLine(modifier: Modifier) {
    Box(
        modifier = modifier
            .height(1.dp)
            .background(NcAccentColor)
    )
}