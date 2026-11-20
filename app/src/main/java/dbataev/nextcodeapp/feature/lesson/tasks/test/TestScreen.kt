package dbataev.nextcodeapp.feature.lesson.tasks.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.common.parsers.highlightTaskCode
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.theme.*
import dbataev.nextcodeapp.feature.lesson.tasks.*

@Composable
fun TestScreen(task: TaskDto, lessonId: Long, modifier: Modifier = Modifier,
    viewModel: TaskAnswerViewModel = viewModel(key = "task-${task.id}"), onBack: (() -> Unit)? = null,
    onTaskComplete: () -> Unit) {
    TaskLayout(title = "Выбор ответа", question = task.question, loading = viewModel.loading,
        canSubmit = viewModel.selected.isNotEmpty(), error = viewModel.error, modifier = modifier,
        onSubmit = { viewModel.submit(task, lessonId, onTaskComplete) }, onBack = onBack) {
        Text(if (task.multipleSelect) "Можно выбрать несколько вариантов" else "Выберите один вариант",
            color = NcAccentColorWeak, fontSize = 14.sp)
        Column(Modifier.fillMaxWidth().then(if (!task.multipleSelect) Modifier.selectableGroup() else Modifier),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            task.options.orEmpty().forEach { option ->
                val selected = option.id in viewModel.selected
                val shape = RoundedCornerShape(16.dp)
                val interaction = if (task.multipleSelect) Modifier.toggleable(value = selected,
                    enabled = !viewModel.loading, role = Role.Checkbox,
                    onValueChange = { viewModel.toggle(option.id, it, true) })
                else Modifier.selectable(selected = selected, enabled = !viewModel.loading, role = Role.RadioButton,
                    onClick = { viewModel.toggle(option.id, true, false) })
                Row(Modifier.fillMaxWidth().heightIn(min = 64.dp)
                    .background(if (selected) NcSecondAccentColor.copy(alpha = 0.12f) else NcMainColor, shape)
                    .border(if (selected) 2.dp else 1.dp, if (selected) NcSecondAccentColor else NcSecondColor, shape)
                    .then(interaction)
                    .padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (task.multipleSelect) Checkbox(checked = selected, onCheckedChange = null,
                        colors = CheckboxDefaults.colors(checkedColor = NcSecondAccentColor,
                            uncheckedColor = NcAccentColorWeak, checkmarkColor = NcAccentColor))
                    else RadioButton(selected = selected, onClick = null,
                        colors = RadioButtonDefaults.colors(selectedColor = NcSecondAccentColor, unselectedColor = NcAccentColorWeak))
                    Spacer(Modifier.width(12.dp))
                    Text(highlightTaskCode(option.text), style = DefaultAppTextStyles.code.copy(color = NcAccentColor),
                        modifier = Modifier.weight(1f))
                }
            }
        }
    }
    viewModel.feedback?.let { ExplanationBottomSheet(it, viewModel::dismiss, viewModel::dismiss) }
}
