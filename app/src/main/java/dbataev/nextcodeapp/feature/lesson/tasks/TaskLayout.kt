package dbataev.nextcodeapp.feature.lesson.tasks

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dbataev.nextcodeapp.R
import dbataev.nextcodeapp.core.designsystem.component.NextCodeButton
import dbataev.nextcodeapp.core.designsystem.component.NextCodeMessageCard
import dbataev.nextcodeapp.core.designsystem.theme.*

@Composable
fun TaskLayout(title: String, question: String, loading: Boolean, canSubmit: Boolean,
    error: String?, onSubmit: () -> Unit, onBack: (() -> Unit)?, modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit) {
    val focus = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    Column(modifier.fillMaxSize().background(NcBackgroundColor).imePadding()) {
        Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) IconButton(onClick = onBack) {
                Icon(painterResource(R.drawable.ic_arrow_down), "Назад", tint = NcAccentColor,
                    modifier = Modifier.size(20.dp).rotate(90f))
            }
            Column(Modifier.padding(horizontal = 8.dp)) {
                Text("ПРАКТИКА", color = NcAccentColorWeak, fontSize = 11.sp, letterSpacing = 1.sp)
                Text(title, color = NcAccentColor, style = DefaultAppTextStyles.bebasRegular24)
            }
        }
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).testTag("task-scroll")) {
            NextCodeMessageCard(question, Modifier.fillMaxWidth().padding(horizontal = 4.dp))
            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)) {
                content()
                error?.let {
                    Text(it, color = NcAccentColor, fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth().background(NcErrorColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                            .border(1.dp, NcErrorColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp)).padding(12.dp))
                }
            }
        }
        Box(Modifier.fillMaxWidth().background(NcMainColor).padding(horizontal = 16.dp, vertical = 12.dp)) {
            NextCodeButton(text = if (loading) "Проверяем…" else "Проверить", enabled = canSubmit && !loading,
                modifier = Modifier.fillMaxWidth().testTag("task-submit"), onClick = { focus.clearFocus(); keyboard?.hide(); onSubmit() }) { }
        }
    }
}
