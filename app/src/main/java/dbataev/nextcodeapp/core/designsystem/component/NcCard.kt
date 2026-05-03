package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.R
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp, top = 8.dp) // Отступ под цитатой
            .animateContentSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(NcSecondColor)
                .clickable { expanded = !expanded }
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
                            .padding(top = 22.dp)
                    )
                }
            } else {
                onCollapsed()
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
            .clickable { expanded = !expanded },
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
                modifier = Modifier.padding(top = 10.dp)
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
                text = text,
                color = NcAccentColor,
                // style = DefaultAppTextStyles.bebasBookXP,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}


