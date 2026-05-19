package dbataev.nextcodeapp.feature.lesson.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.R
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColorWeak
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplanationBottomSheet(
    explanation: String?,
    onDismiss: () -> Unit,
    onRetryClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.Transparent,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = NcBackgroundColor,
                    shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                )
                .border(
                    width = 2.dp,
                    color = NcSecondAccentColor,
                    shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                )
                .padding(24.dp)
        ) {

            Column(Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ai_mask_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)

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

                Box(Modifier.padding(top = 12.dp)) {
                    Column() {
                        Text(
                            text = "Пояснение к заданию",
                            style = DefaultAppTextStyles.bebasRegular32,
                            color = NcAccentColor
                        )

                        Text(
                            text = explanation
                                ?: "Похоже пояснение не загрузилось, как видишь даже разработчик приложения по программированию может ошибиться, попробуй еще раз",
                            style = DefaultAppTextStyles.bebasBook24,
                            color = NcAccentColorWeak
                        )
                    }
                }
            }
            NextCodeButton(
                text = "Попробовать снова",
                onClick = onRetryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) { }
        }
    }
}