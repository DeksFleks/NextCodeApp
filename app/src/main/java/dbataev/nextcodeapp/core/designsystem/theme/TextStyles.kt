package dbataev.nextcodeapp.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class AppTextStyles(
    val hero: TextStyle,        // 96
    val bebasRegular32: TextStyle,
    val bigTitle: TextStyle,    // 36
    val secondText: TextStyle,  // 24
    val mainText: TextStyle,    // 20
    val monoText: TextStyle,    // 20 Courier New
    val smallText: TextStyle,    // 16
    val bebasBook24: TextStyle,
    val bebasBold24: TextStyle,
    val bebasRegular24: TextStyle,
    val bebasBookXP: TextStyle,
    val bebasBookCourse: TextStyle,
    val bebasBook20: TextStyle,
    val bebasBold48: TextStyle,
    val bebasBold36: TextStyle,
    val bebasBook14: TextStyle
) {}

val DefaultAppTextStyles = AppTextStyles(
    hero = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W700,
        fontSize = 96.sp,
        lineHeight = 96.sp
    ),

    bebasRegular32 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp
    ),

    bigTitle = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W700,
        fontSize = 36.sp,
        lineHeight = 36.sp
    ),

    secondText = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 28.sp
    ),
    mainText = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    monoText = TextStyle(
        fontFamily = CourierNew,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    smallText = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W300,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    bebasBook24 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W300,
        fontSize = 28.sp
    ),
    bebasRegular24 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp
    ),
    bebasBookXP = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    bebasBold24 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp
    ),
    bebasBookCourse = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W500,
        fontSize = 36.sp
    ),
    bebasBook20 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp
    ),
    bebasBook14 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    bebasBold48 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W700,
        fontSize = 48.sp
    ),
    bebasBold36 = TextStyle(
        fontFamily = BebasNeue,
        fontWeight = FontWeight.W700,
        fontSize = 36.sp
    )
)