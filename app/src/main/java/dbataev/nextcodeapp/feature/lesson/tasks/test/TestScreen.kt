package dbataev.nextcodeapp.feature.lesson.tasks.test
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dbataev.nextcodeapp.core.data.remote.dto.TaskDto
import dbataev.nextcodeapp.core.designsystem.component.*
import dbataev.nextcodeapp.feature.lesson.tasks.*

@Composable
fun TestScreen(task: TaskDto, lessonId: Long, modifier: Modifier = Modifier,
    viewModel: TaskAnswerViewModel = viewModel(key = "task-${task.id}"), onTaskComplete: () -> Unit) {
    Column(modifier.fillMaxSize()) {
        NcNullTopBar(modifier)
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            NextCodeMessageCard(text = task.question)
            Text(if (task.multipleSelect) "Выберите все верные ответы" else "Выберите один ответ", Modifier.padding(16.dp))
            task.options.orEmpty().forEach { option ->
                NextCodeTestButton(text = option.text, checked = option.id in viewModel.selected,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onCheckedChange = { viewModel.toggle(option.id, it, task.multipleSelect) }) { }
            }
            viewModel.error?.let { Text(it, Modifier.padding(16.dp)) }
        }
        NextCodeButton(text = if (viewModel.loading) "Проверяем…" else "Проверить",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            onClick = { viewModel.submit(task, lessonId, onTaskComplete) }) { }
    }
    viewModel.feedback?.let { message ->
        ExplanationBottomSheet(explanation = message, onDismiss = viewModel::dismiss, onRetryClick = viewModel::dismiss)
    }
}
