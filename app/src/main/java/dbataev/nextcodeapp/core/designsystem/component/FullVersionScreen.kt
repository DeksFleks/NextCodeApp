package dbataev.nextcodeapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dbataev.nextcodeapp.core.designsystem.theme.*

@Composable
fun FullVersionScreen(title: String, description: String) {
    Box(Modifier.fillMaxSize().background(NcBackgroundColor), contentAlignment = Alignment.Center) {
        NcBackgroundText()
        Column(
            Modifier.fillMaxWidth().padding(24.dp).verticalScroll(rememberScrollState())
                .background(NcMainColor, RoundedCornerShape(24.dp))
                .border(1.dp, NcSecondAccentColor, RoundedCornerShape(24.dp)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(title, color = NcSecondAccentColor, style = DefaultAppTextStyles.hero, textAlign = TextAlign.Center)
            Text("Доступно в полной версии продукта", color = NcAccentColor, textAlign = TextAlign.Center)
            Text(description, color = NcAccentColorWeak, textAlign = TextAlign.Center)
            Text("Сейчас ты используешь автономную демоверсию NextCode.", color = NcAccentColorWeak, textAlign = TextAlign.Center)
        }
    }
}
