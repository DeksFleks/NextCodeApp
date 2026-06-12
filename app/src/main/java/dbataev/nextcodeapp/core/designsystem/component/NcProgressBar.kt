package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcThirdAccentColor

@Composable
fun NextCodeProgressTopbar(
    level: Int,
    nickname: String,
    currentXp: Int,
    maxXp: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(220.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .offset(y = 6.dp),
                text = "$level LVL",
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasBold20
            )

            Text(
                modifier = Modifier
                    .offset(y = 6.dp),
                text = nickname.uppercase(),
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasRegular20
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        NextCodeXpBar(
            currentXp = currentXp,
            maxXp = maxXp
        )
    }
}

@Composable
fun NextCodeXpBar(
    currentXp: Int,
    maxXp: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (maxXp > 0) {
        (currentXp.toFloat() / maxXp.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val barShape = GenericShape { size, _ ->
        moveTo(10f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width - 10f, size.height)
        lineTo(0f, size.height)
        close()
    }

    Box(
        modifier = modifier
            .width(220.dp)
            .height(34.dp)
            .clip(barShape)
            .background(NcBackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(barShape)
                .background(NcSecondAccentColor)
        )

        Text(
            text = "$currentXp/$maxXp",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 22.dp)
                .offset(y = 3.dp),
            color = NcAccentColor,
            style = DefaultAppTextStyles.bebasBookXP
        )
    }
}

@Composable
fun NextCodeAchievementsProgressBar(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (max <= 0) {
        0f
    } else {
        (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)
    }

    val percent = (progress * 100).toInt()
    val isCompleted = progress >= 1f

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(
                        color = NcBackgroundColor,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NcThirdAccentColor,
                                    NcSecondAccentColor
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        )
                )
            }

//            if (isCompleted) {
//                Row(
//                    modifier = Modifier.align(Alignment.Center),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Box(
////                        modifier = Modifier
////                            .background(color = Color(0xFF282735))
////                            .padding(horizontal = 4.dp)
//                    ) {
//                        Text(
//                            text = "COMPLETED",
//                            color = NcAccentColor,
//                            style = DefaultAppTextStyles.bebasBook14.copy(
//                                shadow = Shadow(
//                                    color = NcBackgroundColor,
//                                    offset = Offset(0f, 0f),
//                                    blurRadius = 0f
//                                )
//                            ),
//
//                            modifier = Modifier
//                                .offset(y = 1.dp)
//                        )
//                    }
//                }
//            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$percent%",
            color = NcAccentColor,
            style = DefaultAppTextStyles.bebasBook14
        )

        Spacer(modifier = Modifier.width(8.dp))
    }
}