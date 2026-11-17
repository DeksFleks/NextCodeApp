package dbataev.nextcodeapp.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.component.NcBackgroundText
import dbataev.nextcodeapp.core.designsystem.theme.*

/** Keeps the existing card style, but measures its actual contents instead of a screen fraction. */
@Composable
fun AuthFormLayout(title: String, footer: String, onFooterClick: () -> Unit,
    notice: String? = null, content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(NcBackgroundColor)) {
        NcBackgroundText()
        BoxWithConstraints(Modifier.fillMaxSize().imePadding()) {
            Column(
                Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight).padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.widthIn(max = 480.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(36.dp))
                        .border(1.dp, NcSecondAccentColor, RoundedCornerShape(36.dp))
                        .background(NcMainColor).padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(title, color = NcAccentColor, style = DefaultAppTextStyles.bebasBold36)
                    notice?.let { Text(it, color = NcAccentColor, style = DefaultAppTextStyles.mainText) }
                    content()
                }
                Spacer(Modifier.height(24.dp))
                Button(onClick = onFooterClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NcBackgroundColor)) {
                    Text(footer, color = NcLessonBlockedColor, style = DefaultAppTextStyles.bebasBook14)
                }
            }
        }
    }
}
