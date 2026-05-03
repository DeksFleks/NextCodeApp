package dbataev.nextcodeapp.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.R
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

@Immutable
data class BottomMenuItem(
    @DrawableRes val iconRes: Int,
    val contentDescription: String
)

@Composable
fun NextCodeBottomMenu(
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: List<BottomMenuItem> = listOf(
        BottomMenuItem(R.drawable.ic_code, "Код"),
        BottomMenuItem(R.drawable.ic_leaderbord, "Таблица лидеров"),
        BottomMenuItem(R.drawable.ic_trophy, "Достижения"),
        BottomMenuItem(R.drawable.ic_profile, "Профиль")
    )
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(NcMainColor),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ){
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clickable { onItemClick(index) },
                    contentAlignment = Alignment.BottomCenter
                ){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = if (isSelected) 0.dp else 0.dp)
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 24.dp,
                                        topEnd = 24.dp,
                                        bottomStart = 0.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .background(if (isSelected) NcSecondColor else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = item.contentDescription,
                                tint = NcAccentColor,
                                modifier = Modifier.size(28.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(if (isSelected) NcSecondAccentColor else Color.Transparent)
                                    .align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .background(NcSecondColor)
        )
    }
}