package dbataev.nextcodeapp.feature.lesson.tasks.write

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.common.TaskType
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.theme.*
import dbataev.nextcodeapp.feature.lesson.tasks.*

@Composable
fun WriteScreen(task: TaskDto, lessonId: Long, modifier: Modifier = Modifier,
    viewModel: TaskAnswerViewModel = viewModel(key = "task-${task.id}"), onBack: (() -> Unit)? = null,
    onTaskComplete: () -> Unit) {
    LaunchedEffect(task.id) { viewModel.initialize(task) }
    val blank = task.type == TaskType.FILL_BLANK
    TaskLayout(title = if (blank) "Пропуск в коде" else "Написание кода", question = task.question,
        loading = viewModel.loading, canSubmit = viewModel.input.isNotBlank(), error = viewModel.error,
        modifier = modifier, onSubmit = { viewModel.submit(task, lessonId, onTaskComplete) }, onBack = onBack) {
        if (blank) {
            Text("Дополните строку в выделенном поле", color = NcAccentColorWeak, fontSize = 14.sp)
            FillBlankCode(task, viewModel.input, !viewModel.loading) { viewModel.input = it }
        } else {
            CodingEditor(task, viewModel.input, !viewModel.loading) { viewModel.input = it }
            Text("Повторите строку по образцу: регистр и пробелы учитываются. Это упражнение без запуска кода.",
                color = NcAccentColorWeak, fontSize = 13.sp, lineHeight = 19.sp)
        }
    }
    viewModel.feedback?.let { ExplanationBottomSheet(it, viewModel::dismiss, viewModel::dismiss) }
}

@Composable
private fun FillBlankCode(task: TaskDto, input: String, enabled: Boolean, onChange: (String) -> Unit) {
    val codeStyle = TextStyle(fontFamily = CourierNew, color = NcAccentColor, fontSize = 18.sp, lineHeight = 28.sp)
    val shape = RoundedCornerShape(16.dp)
    Column(Modifier.fillMaxWidth().background(NcCodeCardColor, shape).border(1.dp, NcSecondColor, shape).padding(16.dp)) {
        Text("СТРОКА КОДА", color = NcAccentColorWeak, fontSize = 11.sp, letterSpacing = 1.sp)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if (!task.sentenceBefore.isNullOrEmpty()) Text(task.sentenceBefore, style = codeStyle)
            BasicTextField(value = input, onValueChange = onChange, enabled = enabled, singleLine = true,
                textStyle = codeStyle.copy(textAlign = TextAlign.Center), cursorBrush = SolidColor(NcAccentColor),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                modifier = Modifier.width(((input.length.coerceAtLeast(3) * 11f * LocalDensity.current.fontScale) + 24f).coerceIn(72f, 240f).dp).testTag("blank-input")
                    .background(NcSecondAccentColor.copy(alpha = 0.16f), RoundedCornerShape(8.dp))
                    .border(1.dp, NcSecondAccentColor, RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 10.dp),
                decorationBox = { field ->
                    Box(contentAlignment = Alignment.Center) {
                        if (input.isEmpty()) Text("…", style = codeStyle.copy(color = NcAccentColorWeak))
                        field()
                    }
                })
            if (!task.sentenceAfter.isNullOrEmpty()) Text(task.sentenceAfter, style = codeStyle)
        }
    }
}

@Composable
private fun CodingEditor(task: TaskDto, input: String, enabled: Boolean, onChange: (String) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(Modifier.fillMaxWidth().background(NcCodeCardColor, shape).border(1.dp, NcSecondColor, shape)) {
        Row(Modifier.fillMaxWidth().background(NcMainColor, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)).padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Ваш код", color = NcAccentColor, fontSize = 14.sp)
            Text(task.programmingLanguage.orEmpty().uppercase(), color = NcAccentColorWeak, fontFamily = CourierNew, fontSize = 12.sp)
        }
        BasicTextField(value = input, onValueChange = onChange, enabled = enabled,
            textStyle = TextStyle(fontFamily = CourierNew, color = NcAccentColor, fontSize = 17.sp, lineHeight = 26.sp),
            cursorBrush = SolidColor(NcAccentColor),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false, keyboardType = KeyboardType.Ascii),
            modifier = Modifier.fillMaxWidth().heightIn(min = 184.dp, max = 360.dp).padding(16.dp).testTag("coding-input"),
            decorationBox = { field ->
                Box {
                    if (input.isEmpty()) Text("Введите код…", color = NcAccentColorWeak, fontFamily = CourierNew, fontSize = 17.sp)
                    field()
                }
            })
    }
}
