package dbataev.nextcodeapp.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import dbataev.nextcodeapp.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.common.LessonState
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcCourseBlockedColor
import dbataev.nextcodeapp.core.designsystem.theme.NcErrorColor
import dbataev.nextcodeapp.core.designsystem.theme.NcLessonBlockedColor
import dbataev.nextcodeapp.core.designsystem.theme.NcLessonCompletedColor
import dbataev.nextcodeapp.core.designsystem.theme.NcLessonCompletedGlowColor
import dbataev.nextcodeapp.core.designsystem.theme.NcLessonCurrentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcLessonCurrentGlowColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

@Composable
fun NextCodeButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    function: () -> Unit
) {
    Box(
        modifier = modifier
            .width(310.dp)
            .height(50.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NcSecondAccentColor),
            contentPadding = PaddingValues(10.dp)
        ) {
            Text(
                text = text,
                color = NcAccentColor,
                style = DefaultAppTextStyles.bigTitle
            )
        }
    }
}

@Composable
fun NextCodeGrayButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isActive: Boolean
) {
    Box(
        modifier = modifier
            .height(45.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isActive) NcSecondColor else NcCourseBlockedColor,
                contentColor = NcAccentColor
            ),
            contentPadding = PaddingValues(
                top = 10.dp,
                start = 5.dp,
                end = 5.dp,
                bottom = 5.dp
            )
        ) {
            Text(
                text = text,
                color = if (isActive) NcAccentColor else NcLessonBlockedColor,
                style = DefaultAppTextStyles.bebasRegular32
            )
        }
    }
}

@Composable
fun NextCodeCourseButton(
    item: BottonItem = BottonItem(R.drawable.ic_course, "Курс"),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(36.dp)
            .background(NcBackgroundColor, shape = RoundedCornerShape(7.dp))
            .border(
                color = NcSecondAccentColor,
                width = 1.dp,
                shape = RoundedCornerShape(7.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = onClick,
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NcBackgroundColor),
            contentPadding = PaddingValues(3.dp)
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = item.contentDescription,
                tint = NcAccentColor,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun NextCodeStreakButton(
    steak: Int,
    modifier: Modifier = Modifier,
    item: BottonItem = BottonItem(R.drawable.ic_streak, "Стрик"),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(80.dp)
            .height(36.dp)
            .background(NcBackgroundColor, shape = RoundedCornerShape(7.dp))
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NcBackgroundColor),
            contentPadding = PaddingValues(3.dp),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = steak.toString(),
                    color = NcAccentColor,
                    style = DefaultAppTextStyles.bebasBold24,
                    modifier = Modifier
                        .padding(end = 5.dp, top = 4.dp)
                )
                Icon(
                    painter = painterResource(item.iconRes),
                    contentDescription = item.contentDescription,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
fun NextCodeTestButton(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth(0.90f)
            .padding(vertical = 6.dp)
            .height(56.dp)
            .background(
                color = if (checked) NcMainColor else NcBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = NcSecondAccentColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onCheckedChange(!checked)
            }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = NcSecondAccentColor,
                    uncheckedColor = NcSecondAccentColor,
                    checkmarkColor = NcAccentColor
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                color = Color.White
            )
        }
    }
}


@Composable
fun NextCodeLessonButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    lessonId: Long,
    state: LessonState
) {
    Box(
        modifier = modifier
            .width(115.dp)
            .height(90.dp)
            .background(NcSecondColor, shape = RoundedCornerShape(15.dp))
            .border(
                width = 1.dp,
                if (state == LessonState.CURRENT) NcLessonCurrentGlowColor
                else if (state == LessonState.COMPLETED) NcLessonCompletedGlowColor
                else NcLessonBlockedColor,
                shape = RoundedCornerShape(15.dp)
            )
            // .drawBehind()
    ) {
        Button(
            modifier = modifier
                .fillMaxSize(),
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NcSecondColor.copy(alpha = 0f)),
        ) {
            var item: BottonItem

            if (state == LessonState.CURRENT) {
                item = BottonItem(R.drawable.ic_current, "Играть")
            } else if (state == LessonState.COMPLETED) {
                item = BottonItem(R.drawable.ic_completed, "Просмотрено")
            } else {
                item = BottonItem(R.drawable.ic_blocked, "Закрыто")
            }

            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = item.contentDescription,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(130.dp)
            )
        }

    }
}


@Immutable
data class BottonItem(
    @DrawableRes val iconRes: Int,
    val contentDescription: String
) {}


