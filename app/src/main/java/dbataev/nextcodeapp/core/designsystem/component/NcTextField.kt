package dbataev.nextcodeapp.core.designsystem.component

import dbataev.nextcodeapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.theme.DefaultAppTextStyles
import dbataev.nextcodeapp.core.designsystem.theme.NcAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcBackgroundColor
import dbataev.nextcodeapp.core.designsystem.theme.NcErrorColor
import dbataev.nextcodeapp.core.designsystem.theme.NcMainColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondAccentColor
import dbataev.nextcodeapp.core.designsystem.theme.NcSecondColor

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

    var isPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val borderColor = if (errorText != null) {
        NcErrorColor
    } else {
        NcSecondAccentColor
    }

    Box(
        modifier = modifier.height(65.dp)
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
                .padding(start = 15.dp, end = if (isPassword) 5.dp else 15.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = when {
                    !isPassword -> VisualTransformation.None
                    isPasswordVisible -> VisualTransformation.None
                    else -> PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isPassword) {
                        KeyboardType.Password
                    } else {
                        KeyboardType.Text
                    }
                ),
                textStyle = DefaultAppTextStyles.inputCode.copy(
                    color = NcAccentColor
                ),
                cursorBrush = SolidColor(NcAccentColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
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

                        if (isPassword) {
                            IconButton(
                                onClick = {
                                    isPasswordVisible = !isPasswordVisible
                                },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isPasswordVisible) {
                                            R.drawable.ic_visibility_off
                                        } else {
                                            R.drawable.ic_visibility
                                        }
                                    ),
                                    contentDescription = if (isPasswordVisible) {
                                        "Скрыть пароль"
                                    } else {
                                        "Показать пароль"
                                    },
                                    tint = NcAccentColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
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
                    .padding(start = 15.dp)
                    .offset(y = (-5).dp)
                    .background(NcBackgroundColor)
                    .padding(horizontal = 6.dp)
            )
        }
    }
}

@Composable
fun NextCodeTextFieldCode(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Введите ответ...",
    singleLine: Boolean = false
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                style = DefaultAppTextStyles.code
            )
        },
        textStyle = DefaultAppTextStyles.code.copy(
            color = NcAccentColor
        ),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 4,
        maxLines = if (singleLine) 1 else 8,
        shape = RoundedCornerShape(20.dp),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NcMainColor,
            unfocusedContainerColor = NcMainColor,
            focusedBorderColor = NcSecondAccentColor,
            unfocusedBorderColor = NcSecondAccentColor,
            cursorColor = NcAccentColor,
            focusedTextColor = NcAccentColor,
            unfocusedTextColor = NcAccentColor
        )
    )
}