package dbataev.nextcodeapp.core.designsystem.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppBorders {
    val purpleGlowBrush = Brush.linearGradient(
        listOf(
            Color(0xFFB026FF),
            Color(0xFF6A00FF)
        )
    )

    val greenGlowBrush = Brush.linearGradient(
        listOf(
            Color(0xFF00FFB2),
            Color(0xFF00C853)
        )
    )

    val neutralBrush = Brush.linearGradient(
        listOf(
            Color(0x33FFFFFF),
            Color(0x1AFFFFFF)
        )
    )

    val borderOneDp = BorderStroke(1.dp, NcSecondAccentColor)
    val borderTwoDp = BorderStroke(2.dp, NcSecondAccentColor)
}

object AppBorderWidths {
    val thin = 1.dp
    val normal = 1.5.dp
    val strong = 2.dp
}