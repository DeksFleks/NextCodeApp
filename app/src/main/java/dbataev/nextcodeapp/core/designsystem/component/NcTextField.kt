package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcErrorColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor

@Composable
fun NextCodeTextField(
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    errorText: String? = null
) {
    val shape = RoundedCornerShape(16.dp)
    val labelText = errorText ?: placeholderText
    val borderColor = if (errorText != null) NcErrorColor else NcSecondAccentColor
    val labelColor = if (errorText != null) NcErrorColor else NcSecondAccentColor

    Box(
        modifier = modifier
            .height(65.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .align(Alignment.BottomCenter)
                .clip(shape)
                .background(NcBackgroundColor)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = shape
                )
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (isPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                textStyle = DefaultAppTextStyles.bebasBook24.copy(
                    color = NcAccentColor,
                ),
                cursorBrush = SolidColor(NcAccentColor),
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 3.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholderText,
                                style = DefaultAppTextStyles.bebasBook24,
                                color = NcAccentColor.copy(alpha = 0.45f)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        if (errorText != null) {
            Text(
                text = errorText,
                style = DefaultAppTextStyles.bebasBook14,
                color = NcAccentColor,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 9.dp)
                    .padding(horizontal = 6.dp)
                    .offset(x = 0.dp, y = -5.dp)
            )
        }
    }
}