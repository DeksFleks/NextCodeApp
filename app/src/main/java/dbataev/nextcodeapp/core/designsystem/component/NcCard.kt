package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.R
import dbataev.nextcodeapp.core.data.remote.dto.AchievementDto
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcCodeColor
import dbataev.nextcodeapp.core.designsystem.theme.NcCourseBlockedColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor
import dbataev.nextcodeapp.core.designsystem.theme.NcThirdAccentColor

@Composable
fun NextCodeQuoteDropdown(
    quote: String,
    author: String,
    modifier: Modifier = Modifier,
    onCollapsed: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "quote_rotation"
    )

    LaunchedEffect(expanded) {
        if (!expanded) {
            kotlinx.coroutines.delay(100)
            onCollapsed()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp, top = 8.dp)
            .animateContentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(NcSecondColor)
                .clickable {
                    expanded = !expanded
                }
                .padding(
                    start = 28.dp,
                    top = if (expanded) 24.dp else 0.dp,
                    end = 28.dp,
                    bottom = if (expanded) 8.dp else 0.dp
                )
        ) {
            if (expanded) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = quote,
                        color = Color.White,
                        style = DefaultAppTextStyles.bebasRegular24,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = "© $author",
                        color = Color.White,
                        style = DefaultAppTextStyles.bebasBookXP,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 22.dp, bottom = 8.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .offset(x = 40.dp, y = -30.dp)
            .size(width = 68.dp, height = 30.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(NcSecondColor)
            .border(
                width = 3.dp,
                color = NcBackgroundColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable {
                expanded = !expanded
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = if (expanded) "Скрыть цитату" else "Показать цитату",
            tint = Color.White,
            modifier = Modifier
                .size(14.dp)
                .rotate(rotation)
        )
    }
}

@Composable
fun NextCodeMessageCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        // 1. Слой карточки: фон + бордер
        Box(
            modifier = Modifier
                .matchParentSize()
                .defaultMinSize(minHeight = 120.dp)
                .background(
                    color = NcMainColor,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    color = NcSecondAccentColor,
                    width = 2.dp,
                    shape = RoundedCornerShape(24.dp)
                )
        )

        // 2. Контент выше бордера
        Column(
            modifier = Modifier
                .defaultMinSize(minHeight = 120.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .offset(x = -30.dp, y = -10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ai_mask_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .border(
                            width = 7.dp,
                            color = NcBackgroundColor,
                            shape = CircleShape
                        )
                )

                Text(
                    text = "AI Mask",
                    color = NcSecondAccentColor,
                    style = DefaultAppTextStyles.bebasRegular32,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(start = 5.dp, bottom = 15.dp)
                )
            }

            Text(
                text = parseNextCodeText(text),
                color = NcAccentColor,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
fun NextCodeStatisticsProfileCard(
    statistics: String,
    text: String,
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.ic_lvl_icon
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .height(140.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3A344A),
                        Color(0xFF2C2C3B),
                        Color(0xFF1F1F2B)
                    )
                ),
                shape = shape
            )
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NcThirdAccentColor,
                        Color(0xFF6A00FF)
                    )
                ),
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NcThirdAccentColor.copy(alpha = 0.14f),
                            Color.Transparent
                        )
                    ),
                    shape = shape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color(0xFF9B2DFF),
                modifier = Modifier
                    .size(60.dp)
                    .offset(y = -6.dp)
            )

            Text(
                text = statistics,
                color = NcAccentColor,
                style = DefaultAppTextStyles.bebasBold36
            )

            Text(
                text = text,
                color = NcCourseBlockedColor,
                style = DefaultAppTextStyles.bebasRegular20
            )
        }
    }
}

@Composable
fun NextCodeAchievementCard(
    modifier: Modifier = Modifier,
    achievement: AchievementDto,
) {
    val shape = RoundedCornerShape(16.dp)

    Box(modifier = modifier
        .fillMaxWidth(1f)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF3A344A),
                    Color(0xFF2C2C3B),
                    Color(0xFF1F1F2B)
                )
            ),
            shape = shape
        )
        .border(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    NcThirdAccentColor,
                    Color(0xFF6A00FF)
                )
            ),
            shape = shape
        )
    ) {
        Row(
            Modifier.padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_achievement_bg),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
            )
            Column(
                Modifier.padding(vertical = 20.dp)
            ) {
                Text(
                    text = achievement.title,
                    style = DefaultAppTextStyles.bebasRegular24,
                    color = NcAccentColor
                )

                Text(
                    text = achievement.description,
                    style = DefaultAppTextStyles.bebasBook20,
                    color = NcCourseBlockedColor
                )

                Text(
                    text = "Тут будет прогресс",
                    style = DefaultAppTextStyles.bebasBook14,
                    color = NcCourseBlockedColor
                )
            }
        }
    }
}

fun parseNextCodeText(input: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0

        while (i < input.length) {
            when {
                input.startsWith("**", i) -> {
                    val end = input.indexOf("**", startIndex = i + 2)

                    if (end != -1) {
                        withStyle(
                            SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(input.substring(i + 2, end))
                        }
                        i = end + 2
                    } else {
                        append(input[i])
                        i++
                    }
                }

                input[i] == '`' -> {
                    val end = input.indexOf('`', startIndex = i + 1)

                    if (end != -1) {
                        withStyle(
                            SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                background = NcSecondColor,
                                color = NcCodeColor
                            )
                        ) {
                            append(input.substring(i + 1, end))
                        }
                        i = end + 1
                    } else {
                        append(input[i])
                        i++
                    }
                }

                else -> {
                    append(input[i])
                    i++
                }
            }
        }
    }
}


